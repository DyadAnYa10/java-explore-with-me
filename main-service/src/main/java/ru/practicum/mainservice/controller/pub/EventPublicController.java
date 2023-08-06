package ru.practicum.mainservice.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.event.EventFullDto;
import ru.practicum.mainservice.dto.event.EventShortDto;
import ru.practicum.mainservice.service.CommentService;
import ru.practicum.mainservice.service.EventPublicService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventPublicController {
    private final EventPublicService eventService;
    private final CommentService commentService;

    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam(name = "text", required = false) String text,
                                         @RequestParam(name = "categories", required = false) List<Long> categories,
                                         @RequestParam(name = "paid", required = false) Boolean paid,
                                         @RequestParam(name = "rangeStart", required = false)
                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
                                         @RequestParam(name = "rangeEnd", required = false)
                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
                                         @RequestParam(name = "onlyAvailable", required = false) Boolean onlyAvailable,
                                         @RequestParam(name = "sort", defaultValue = "event_date") String sort,
                                         @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                         @RequestParam(name = "size", defaultValue = "10") @Positive Integer size,
                                         HttpServletRequest request) {
        log.info("EventPublicController: Запрос на получение всех событий с параметрами запроса: text={}, " +
                        "categories={}, paid={}, rangeStart={}, rangeEnd={}, onlyAvailable={}, sort={}, from={}, " +
                        "size={}, request={}",
                text, categories, paid, startDate, endDate, onlyAvailable, sort, from, size, request.getRequestURI());

        return eventService.getAllEvents(text, categories, paid, startDate, endDate, onlyAvailable, sort,
                from, size, request.getRemoteAddr(), request.getRequestURI());
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable(name = "eventId") @Positive Long eventId,
                                 HttpServletRequest request) {
        log.info("EventPublicController: Запрос на получение события с id='{}'", eventId);
        return eventService.getEventById(eventId, request.getRemoteAddr(), request.getRequestURI());
    }

    @GetMapping("/{eventId}/comments")
    public List<CommentDto> getEventComments(@PathVariable Long eventId,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("EventPublicController запрос на получение комментариев к событию с id='{}'", eventId);
        return commentService.getAllCommentsByEventId(eventId, from, size);
    }
}