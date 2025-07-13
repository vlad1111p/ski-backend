package com.skitrainer.dto.google.calendar;

import java.util.List;

public record GoogleEventListResponse(
        List<GoogleEventResponse> items
) {
}