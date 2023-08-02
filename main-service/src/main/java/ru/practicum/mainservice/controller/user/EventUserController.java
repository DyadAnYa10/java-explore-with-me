package ru.practicum.mainservice.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.event.*;
import ru.practicum.mainservice.dto.request.RequestDto;
import ru.practicum.mainservice.service.EventUserService;
import ru.practicum.mainservice.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class EventUserController {
    private final EventUserService eventService;
    private final RequestService requestService;

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable(name = "userId") @Positive Long userId,
                                    @RequestBody @Valid EventNewDto request) {
        log.info("EventUserController: Request to create event {} from user with id='{}'", request, userId);
        return eventService.createEvent(request, userId);
    }

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getEvents(@PathVariable(name = "userId") @Positive Long userId,
                                         @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                         @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("EventUserController: Request to get events by userId from user with id='{}'", userId);
        return eventService.getEventsByUserId(userId, from, size);
    }


    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventByUserAndEvent(@PathVariable(name = "userId") @Positive Long userId,
                                               @PathVariable(name = "eventId") @Positive Long eventId) {
        log.info("EventUserController: Request to get event by userId='{}' and eventId='{}' from user with id='{}'",
                userId, eventId, userId);
        return eventService.getEventByUserIdAndEventId(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEventByOwner(@PathVariable(name = "userId") @Positive Long userId,
                                           @PathVariable(name = "eventId") @Positive Long eventId,
                                           @RequestBody @Valid EventUpdateUserRequest request) {
        log.info("EventUserController: Request to update event  with id='{}' from user with id='{}', request={}",
                eventId, userId, request);
        return eventService.updateEventByUserIdAndEventId(userId, eventId, request);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestDto> getRequestByEvent(@PathVariable(name = "userId") Long userId,
                                              @PathVariable(name = "eventId") Long eventId) {
        log.info("EventUserController: Request to get requests by eventId='{}' from user with id='{}'",
                eventId, userId);
        return requestService.getAllParticipationRequestsByEventId(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResponse updateStatusRequest(@PathVariable(name = "userId") @Positive Long userId,
                                                                @PathVariable(name = "eventId") @Positive Long eventId,
                                                                @RequestBody EventRequestStatusUpdateRequest request) {
        log.info("EventUserController: Request to update status event with id='{}' from user with id='{}'",
                eventId, userId);
        return requestService.updateStatusRequestByEventId(userId, eventId, request);
    }
}
