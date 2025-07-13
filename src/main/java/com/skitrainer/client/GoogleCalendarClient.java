package com.skitrainer.client;

import com.skitrainer.dto.google.calendar.GoogleEventListResponse;
import com.skitrainer.dto.google.calendar.GoogleEventPayload;
import com.skitrainer.dto.google.calendar.GoogleEventResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "googleCalendar", url = "https://www.googleapis.com/calendar/v3")
public interface GoogleCalendarClient {

    @GetMapping("/calendars/primary/events")
    GoogleEventListResponse getEvents(
            @RequestHeader("Authorization") String bearerToken,
            @RequestParam("maxResults") int maxResults,
            @RequestParam("singleEvents") boolean singleEvents,
            @RequestParam("orderBy") String orderBy
    );

    @PostMapping("/calendars/primary/events")
    GoogleEventResponse createEvent(
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody GoogleEventPayload event
    );

    @DeleteMapping("/calendars/primary/events/{eventId}")
    void deleteEvent(
            @RequestHeader("Authorization") String bearerToken,
            @PathVariable("eventId") String eventId
    );
}
