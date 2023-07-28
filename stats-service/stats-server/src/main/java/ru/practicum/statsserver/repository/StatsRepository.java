package ru.practicum.statsserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.statsdto.StatDto;
import ru.practicum.statsserver.model.ItemStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<ItemStats, Long> {
    @Query("select new ru.practicum.statsdto.StatDto(h.app, h.uri, count(h.ip)) " +
            "from ItemStats h " +
            "where h.timestamp between ?1 and ?2 " +
            "group by h.app, h.uri " +
            "order by count(h.ip) desc")
    List<StatDto> findAllWithoutUris(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.statsdto.StatDto(h.app, h.uri, count(h.ip)) " +
            "from ItemStats h " +
            "where h.timestamp between ?1 and ?2 " +
            "and h.uri in (?3) " +
            "group by h.app, h.uri " +
            "order by count(h.ip) desc")
    List<StatDto> findAllNotUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.statsdto.StatDto(h.app, h.uri, count(distinct h.ip)) " +
            "from ItemStats h " +
            "where h.timestamp between ?1 and ?2 " +
            "and h.uri in (?3) " +
            "group by h.app, h.uri " +
            "order by count(distinct h.ip) desc")
    List<StatDto> findAllUnique(LocalDateTime start, LocalDateTime end, List<String> uris);
}
