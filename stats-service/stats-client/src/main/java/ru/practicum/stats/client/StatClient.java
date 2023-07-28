package ru.practicum.stats.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.statsdto.HitDto;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StatClient {
    private final RestTemplate rest;

    @Value("${stats-server.url}")
    private String serverUrl;

    public ResponseEntity<Object> saveHit(HitDto hitDto) {
        return rest.postForEntity(serverUrl.concat("/hit"),
                hitDto,
                Object.class);
    }

    public ResponseEntity<Object> getStatistics(String start, String end, List<String> uris, Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique);

        return rest.getForEntity(serverUrl.concat("/stats?start={start}&end={end}&uris={uris}&unique={unique}"),
                Object.class,
                parameters);
    }
}