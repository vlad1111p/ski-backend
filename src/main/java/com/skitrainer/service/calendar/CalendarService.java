package com.skitrainer.service.calendar;

import com.skitrainer.model.Event;

import java.util.List;

public interface CalendarService {
    List<Event> getEvents(String userId);

    Event createEvent(String userId, Event event);

    Event updateEvent(String userId, String eventId, Event updatedEvent);

    void deleteEvent(String userId, String eventId);

}
