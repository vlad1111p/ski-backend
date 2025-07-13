package com.skitrainer.service.calendar.impl;

import com.skitrainer.model.Event;
import com.skitrainer.service.calendar.CalendarService;

import java.util.List;

public class GoogleCalendarService implements CalendarService {
    @Override
    public List<Event> getEvents(String userId) {
        return List.of();
    }

    @Override
    public Event createEvent(String userId, Event event) {
        return null;
    }

    @Override
    public void deleteEvent(String userId, String eventId) {

    }
}
