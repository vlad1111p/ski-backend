package com.skitrainer.mapper;

import com.skitrainer.dto.google.calendar.GoogleEventDateTime;
import com.skitrainer.dto.google.calendar.GoogleEventPayload;
import com.skitrainer.dto.google.calendar.GoogleEventResponse;
import com.skitrainer.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface GoogleEventMapper {
    GoogleEventMapper INSTANCE = Mappers.getMapper(GoogleEventMapper.class);

    @Named("toLocalDateTime")
    static LocalDateTime toLocalDateTime(GoogleEventDateTime value) {
        if (value == null || value.dateTime() == null) return null;
        return ZonedDateTime.parse(value.dateTime()).toLocalDateTime();
    }

    @Named("toGoogleDateTime")
    static GoogleEventDateTime toGoogleDateTime(java.time.LocalDateTime value) {
        if (value == null) return null;
        return new GoogleEventDateTime(
                value.atZone(java.time.ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                java.time.ZoneId.systemDefault().getId()
        );
    }

    @Mapping(source = "id", target = "googleEventId")
    @Mapping(source = "summary", target = "title")
    @Mapping(source = "location", target = "location")
    @Mapping(source = "start", target = "startTime", qualifiedByName = "toLocalDateTime")
    @Mapping(source = "end", target = "endTime", qualifiedByName = "toLocalDateTime")
    Event toEvent(GoogleEventResponse response);

    @Mapping(target = "summary", source = "title")
    @Mapping(target = "location", source = "location")
    @Mapping(target = "start", source = "startTime", qualifiedByName = "toGoogleDateTime")
    @Mapping(target = "end", source = "endTime", qualifiedByName = "toGoogleDateTime")
    GoogleEventPayload toGooglePayload(Event event);
}
