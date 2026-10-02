package com.notely.controller;

import com.notely.dto.PadUpdateMessage;
import com.notely.dto.PadWebSocketResponse;
import com.notely.entity.Pad;
import com.notely.service.PadService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class PadWebSocketController {

    private final PadService padService;
    private final SimpMessagingTemplate messagingTemplate;

    public PadWebSocketController(
            PadService padService,
            SimpMessagingTemplate messagingTemplate) {

        this.padService = padService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/pads/{id}")
    public void updatePad(
            @DestinationVariable String id,
            PadUpdateMessage message) {

        try {

            Pad updatedPad = padService.updatePad(
                    id,
                    message.content(),
                    message.version()
            );

            // Send successful update to everyone
            messagingTemplate.convertAndSend(
                    "/topic/pads/" + id,
                    new PadWebSocketResponse(
                            "UPDATE",
                            "Pad updated",
                            updatedPad
                    )
            );

        } catch (PadService.VersionConflictException e) {

            Pad currentPad =
                    padService.getOrCreatePad(id);

            // Send conflict ONLY to the client
            // that caused the conflict
            messagingTemplate.convertAndSendToUser(
                    message.userId(),
                    "/queue/pad-conflict",
                    new PadWebSocketResponse(
                            "CONFLICT",
                            "Pad was modified by another client",
                            currentPad
                    )
            );
        }
    }
}
