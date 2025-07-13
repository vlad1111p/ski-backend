package com.skitrainer.dto.google.calendar;

import com.skitrainer.model.Event;

import java.util.HashMap;
import java.util.Map;

public record GoogleEventPayload(
        String summary,
        String description,
        Map<String, String> start,
        Map<String, String> end,
        String location
) {
    public static GoogleEventPayload fromLocalEvent(Event event) {
        Map<String, String> startTime = new HashMap<>();
        startTime.put("dateTime", event.getStartTime().toString());
        startTime.put("timeZone", "Europe/Berlin");

        Map<String, String> endTime = new HashMap<>();
        endTime.put("dateTime", event.getEndTime().toString());
        endTime.put("timeZone", "Europe/Berlin");

        return new GoogleEventPayload(
                event.getTitle(),
                event.getDescription(),
                startTime,
                endTime,
                event.getLocation()
        );
    }
}
