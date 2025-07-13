package com.skitrainer.service.calendar.impl;

import com.skitrainer.client.GoogleCalendarClient;
import com.skitrainer.dto.google.calendar.GoogleEventListResponse;
import com.skitrainer.dto.google.calendar.GoogleEventPayload;
import com.skitrainer.dto.google.calendar.GoogleEventResponse;
import com.skitrainer.mapper.GoogleEventMapper;
import com.skitrainer.model.Event;
import com.skitrainer.model.User;
import com.skitrainer.repository.UserRepository;
import com.skitrainer.service.calendar.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoogleCalendarService implements CalendarService {

    private final GoogleCalendarClient googleCalendarClient;
    private final UserRepository userRepository;
    private final GoogleEventMapper googleEventMapper;

    @Override
    public List<Event> getEvents(final String userId) {
        final User user = userRepository.findByEmail(userId).orElseThrow();
        final String bearer = "Bearer " + user.getGoogleAccessToken();

        final GoogleEventListResponse response = googleCalendarClient.getEvents(bearer, 50, true, "startTime");
        return response.items().stream()
                .map(googleEventMapper::toEvent)
                .collect(Collectors.toList());
    }

    @Override
    public Event createEvent(final String userId, final Event event) {
        final User user = userRepository.findByEmail(userId).orElseThrow();
        final String bearer = "Bearer " + user.getGoogleAccessToken();

        final GoogleEventPayload payload = GoogleEventPayload.fromLocalEvent(event);
        final GoogleEventResponse response = googleCalendarClient.createEvent(bearer, payload);

        event.setGoogleEventId(response.id());
        event.setGoogleSyncTimestamp(Instant.now());
        return event;
    }

    @Override
    public Event updateEvent(final String userId, final String eventId, final Event updatedEvent) {
        final User user = userRepository.findByEmail(userId).orElseThrow();
        final String bearer = "Bearer " + user.getGoogleAccessToken();

        final GoogleEventPayload payload = GoogleEventPayload.fromLocalEvent(updatedEvent);
        final GoogleEventResponse response = googleCalendarClient.updateEvent(bearer, eventId, payload);

        updatedEvent.setGoogleEventId(response.id());
        updatedEvent.setGoogleSyncTimestamp(Instant.now());
        return updatedEvent;
    }

    @Override
    public void deleteEvent(final String userId, final String eventId) {
        final User user = userRepository.findByEmail(userId).orElseThrow();
        final String bearer = "Bearer " + user.getGoogleAccessToken();

        googleCalendarClient.deleteEvent(bearer, eventId);
    }
}