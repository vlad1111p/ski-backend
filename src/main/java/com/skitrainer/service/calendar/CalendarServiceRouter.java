package com.skitrainer.service.calendar;


import com.skitrainer.model.User;
import com.skitrainer.repository.UserRepository;
import com.skitrainer.service.calendar.impl.GoogleCalendarService;
import com.skitrainer.service.calendar.impl.LocalCalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalendarServiceRouter {
    private final GoogleCalendarService googleCalendarService;
    private final LocalCalendarService localCalendarService;
    private final UserRepository userRepository;

    public CalendarService resolve(final String email) {
        final User user = userRepository.findByEmail(email).orElseThrow();
        return user.isGoogleAuthenticated() ? googleCalendarService : localCalendarService;
    }
}
