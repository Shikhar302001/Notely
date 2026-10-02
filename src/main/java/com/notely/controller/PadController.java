package com.notely.controller;

import com.notely.dto.UpdatePadRequest;
import com.notely.entity.Pad;
import com.notely.service.PadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(
        origins = {
                "http://localhost:5500",
                "http://127.0.0.1:5500"
        }
)
@RequestMapping("/api/pads")
public class PadController {
    private final PadService padService;

    public PadController(PadService padService) {
        this.padService = padService;
    }

    @GetMapping("/{id}")
    public Pad getPad(@PathVariable String id) {
        return padService.getOrCreatePad(id);
    }

    @PutMapping("/{id}")
    public Pad updatePad(
            @PathVariable String id,
            @RequestBody UpdatePadRequest request) {

        return padService.updatePad(id, request.content(),request.version());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePad(@PathVariable String id) {
             padService.deletePad(id);

             return new ResponseEntity<>("Pad has been deleted", HttpStatus.OK);
    }

}
