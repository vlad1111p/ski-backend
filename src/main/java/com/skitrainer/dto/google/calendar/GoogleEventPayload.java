package com.skitrainer.dto.google.calendar;

public record GoogleEventPayload(
        String summary,
        String location,
        GoogleEventDateTime start,
        GoogleEventDateTime end
) {
}
