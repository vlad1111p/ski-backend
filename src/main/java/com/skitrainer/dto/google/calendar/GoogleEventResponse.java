package com.skitrainer.dto.google.calendar;

public record GoogleEventResponse(
        String id,
        String summary,
        String htmlLink
) {
}