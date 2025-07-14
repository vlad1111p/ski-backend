package com.skitrainer.service.calendar.impl;

import com.skitrainer.model.Event;
import com.skitrainer.repository.EventRepository;
import com.skitrainer.service.calendar.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocalCalendarService implements CalendarService {

    private final EventRepository eventRepository;

    @Override
    public List<Event> getEvents(final String userId) {
        return eventRepository.findByUserInvolved(userId);
    }

    @Override
    public Event createEvent(final String userId, final Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(final String userId, final String eventId, final Event updatedEvent) {
        final Event existing = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        existing.setTitle(updatedEvent.getTitle());
        existing.setDescription(updatedEvent.getDescription());
        existing.setStartTime(updatedEvent.getStartTime());
        existing.setEndTime(updatedEvent.getEndTime());
        existing.setLocation(updatedEvent.getLocation());
        existing.setStatus(updatedEvent.getStatus());
        existing.setParticipants(updatedEvent.getParticipants());
        existing.setTrainers(updatedEvent.getTrainers());

        return eventRepository.save(existing);
    }

    @Override
    public void deleteEvent(final String userId, final String eventId) {
        eventRepository.deleteById(eventId);
    }
}
