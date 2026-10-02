package com.notely.dto;

import com.notely.entity.Pad;

public record PadWebSocketResponse(
        String type,
        String message,
        Pad pad
) { }
