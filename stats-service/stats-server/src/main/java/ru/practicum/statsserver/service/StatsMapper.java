package ru.practicum.statsserver.service;

import ru.practicum.statsdto.HitDto;
import ru.practicum.statsserver.model.ItemStats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatsMapper {

    public static HitDto toHitDto(ItemStats itemStats) {
        return HitDto.builder()
                .app(itemStats.getApp())
                .uri(itemStats.getUri())
                .ip(itemStats.getIp())
                .timestamp(itemStats.getTimestamp().toString())
                .build();
    }

    public static ItemStats toItemStats(HitDto hitDto) {
        LocalDateTime dateTime = LocalDateTime.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .parse(hitDto.getTimestamp()));

        return ItemStats.builder()
                .app(hitDto.getApp())
                .uri(hitDto.getUri())
                .ip(hitDto.getIp())
                .timestamp(dateTime)
                .build();
    }
}
