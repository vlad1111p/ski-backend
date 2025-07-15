package com.skitrainer.service.calendar;


import com.skitrainer.model.User;
import com.skitrainer.repository.UserRepository;
import com.skitrainer.service.auth.GoogleOAuthService;
import com.skitrainer.service.calendar.impl.GoogleCalendarService;
import com.skitrainer.service.calendar.impl.LocalCalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CalendarServiceRouter {
    private final GoogleCalendarService googleCalendarService;
    private final LocalCalendarService localCalendarService;
    private final UserRepository userRepository;
    private final GoogleOAuthService googleOAuthService;

    public CalendarService resolve(final String email) {
        final User user = userRepository.findByEmail(email).orElseThrow();

        if (user.isGoogleAuthenticated()) {
            refreshToken(user);
            return googleCalendarService;
        }
        return localCalendarService;
    }

    private void refreshToken(final User user) {
        if (user.getTokenExpiry() == null || user.getTokenExpiry().isBefore(Instant.now())) {
            googleOAuthService.refreshAccessToken(user);
        }
    }
}
