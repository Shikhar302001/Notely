package com.notely.dto;

public record PadUpdateMessage(
        String content,
        Long version,
        String userId
) {
}