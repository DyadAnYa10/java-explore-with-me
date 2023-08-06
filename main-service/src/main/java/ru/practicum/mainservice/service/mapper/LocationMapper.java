package ru.practicum.mainservice.service.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.dto.location.LocationDto;
import ru.practicum.mainservice.entity.Location;

@UtilityClass
public class LocationMapper {
    public Location fromDto(LocationDto locationDto) {
        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }
}
