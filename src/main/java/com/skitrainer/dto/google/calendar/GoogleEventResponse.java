package com.skitrainer.dto.google.calendar;

public record GoogleEventResponse(
        String id,
        String summary,
        String location,
        String htmlLink,
        GoogleEventDateTime start,
        GoogleEventDateTime end
) {
}