package com.skitrainer.controller;

import com.skitrainer.model.Event;
import com.skitrainer.service.calendar.CalendarService;
import com.skitrainer.service.calendar.CalendarServiceRouter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")

@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarServiceRouter calendarServiceRouter;

    @GetMapping
    public ResponseEntity<List<Event>> getEvents(final Principal principal) {
        final String userId = principal.getName();
        final CalendarService service = calendarServiceRouter.resolve(userId);
        return ResponseEntity.ok(service.getEvents(userId));
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody final Event event, final Principal principal) {
        final String userId = principal.getName();
        final CalendarService service = calendarServiceRouter.resolve(userId);
        return ResponseEntity.ok(service.createEvent(userId, event));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<Event> updateEvent(
            @PathVariable final String eventId,
            @RequestBody final Event updated,
            final Principal principal
    ) {
        final String userId = principal.getName();
        final CalendarService service = calendarServiceRouter.resolve(userId);
        return ResponseEntity.ok(service.updateEvent(userId, eventId, updated));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable final String eventId,
            final Principal principal
    ) {
        final String userId = principal.getName();
        final CalendarService service = calendarServiceRouter.resolve(userId);
        service.deleteEvent(userId, eventId);
        return ResponseEntity.noContent().build();
    }
}