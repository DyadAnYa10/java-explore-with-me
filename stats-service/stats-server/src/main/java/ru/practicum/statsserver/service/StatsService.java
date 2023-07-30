package ru.practicum.statsserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.statsdto.HitDto;
import ru.practicum.statsdto.StatDto;
import ru.practicum.statsserver.exception.NoValidParameterRequest;
import ru.practicum.statsserver.model.ItemStats;
import ru.practicum.statsserver.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final StatsRepository repository;

    public void saveRecord(HitDto hitDto) {
        ItemStats itemStats = StatsMapper.toItemStats(hitDto);
        repository.save(itemStats);
    }

    public List<StatDto> getAllStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        checkEndIsAfterStart(start, end);

        if (uris == null || uris.isEmpty()) {
            return repository.findAllWithoutUris(start, end);
        }
        if (unique) {
            return repository.findAllUnique(start, end, uris);
        }
        return repository.findAllNotUnique(start, end, uris);
    }

    private void checkEndIsAfterStart(LocalDateTime start, LocalDateTime end) {
        if (!end.isAfter(start)) {
            throw new NoValidParameterRequest("Date should be valid!");
        }
    }
}
