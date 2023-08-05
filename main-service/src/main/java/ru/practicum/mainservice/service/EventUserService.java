package ru.practicum.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.mainservice.dto.event.EventFullDto;
import ru.practicum.mainservice.dto.event.EventNewDto;
import ru.practicum.mainservice.dto.event.EventShortDto;
import ru.practicum.mainservice.dto.event.EventUpdateUserRequest;
import ru.practicum.mainservice.entity.Category;
import ru.practicum.mainservice.entity.Event;
import ru.practicum.mainservice.entity.User;
import ru.practicum.mainservice.model.exception.EventConflictException;
import ru.practicum.mainservice.model.exception.IncorrectRequestException;
import ru.practicum.mainservice.model.exception.NoFoundObjectException;
import ru.practicum.mainservice.model.EventState;
import ru.practicum.mainservice.repository.EventRepository;
import ru.practicum.mainservice.service.mapper.EventMapper;
import ru.practicum.mainservice.service.mapper.LocationMapper;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EventUserService {
    private static final Integer HOURS_BEFORE_START_EVENT = 2;
    private final EventRepository eventRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    @Transactional
    public EventFullDto createEvent(EventNewDto request, Long userId) {
        checkTimeBeforeEventStart(request.getEventDate());

        User user = userService.getUserByIdIfExist(userId);
        Category category = categoryService.getCategoryByIdIfExist(request.getCategory());

        Event event = EventMapper.toEvent(request, category, user);
        Event savedEvent = eventRepository.save(event);

        return EventMapper.toEventFullDto(savedEvent);
    }

    public List<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        userService.checkExistUserById(userId);

        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageable);
        return EventMapper.toEventShortDtoList(events);
    }

    public EventFullDto getEventByUserIdAndEventId(Long userId, Long eventId) {
        userService.checkExistUserById(userId);
        Event event = getEventByIdAndInitiatorIdIfExist(eventId, userId);
        return EventMapper.toEventFullDto(event);
    }

    public EventFullDto updateEventByUserIdAndEventId(Long userId, Long eventId, EventUpdateUserRequest request) {
        userService.checkExistUserById(userId);

        Event foundEvent = getEventByIdAndInitiatorIdIfExist(eventId, userId);

        if (!Objects.equals(userId, foundEvent.getInitiator().getId())) {
            throw new EventConflictException(String.format("Пользователь с id='%s' не автор события с id='%s'",
                    userId, eventId));
        }

        if (Objects.equals(EventState.PUBLISHED, foundEvent.getState())) {
            throw new EventConflictException("Статус события - 'PUBLISHED'. " +
                    "Статус события должен быть 'PENDING' or 'CANCELED'");
        }

        if (Objects.nonNull(request.getTitle())) {
            foundEvent.setTitle(request.getTitle());
        }
        if (Objects.nonNull(request.getEventDate())) {
            checkTimeBeforeEventStart(request.getEventDate());
            foundEvent.setEventDate(request.getEventDate());
        }
        if (Objects.nonNull(request.getAnnotation()) && StringUtils.hasLength(request.getAnnotation())) {
            foundEvent.setAnnotation(request.getAnnotation());
        }
        if (Objects.nonNull(request.getCategory())) {
            Category category = categoryService.getCategoryByIdIfExist(request.getCategory());
            foundEvent.setCategory(category);
        }
        if (Objects.nonNull(request.getDescription())) {
            foundEvent.setDescription(request.getDescription());
        }
        if (Objects.nonNull(request.getLocation())) {
            foundEvent.setLocation(LocationMapper.toLocation(request.getLocation()));
        }
        if (Objects.nonNull(request.getParticipantLimit())) {
            foundEvent.setParticipantLimit(request.getParticipantLimit());
        }
        if (Objects.nonNull(request.getRequestModeration())) {
            foundEvent.setRequestModeration(request.getRequestModeration());
        }
        if (Objects.nonNull(request.getStateAction())) {
            switch (request.getStateAction()) {
                case SEND_TO_REVIEW:
                    foundEvent.setState(EventState.PENDING);
                    break;
                case CANCEL_REVIEW:
                    foundEvent.setState(EventState.CANCELED);
                    break;
            }
        }

        Event updatedEvent = eventRepository.save(foundEvent);
        return EventMapper.toEventFullDto(updatedEvent);
    }

    public Event getEventByIdAndInitiatorIdIfExist(Long eventId, Long userId) {
        return eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() ->
                new NoFoundObjectException(String.format("Событие с id='%s' и инициатором с id='%s' не найдено",
                        eventId, userId)));
    }

    private void checkTimeBeforeEventStart(LocalDateTime startDate) {
        LocalDateTime munTimePeriod = LocalDateTime.now().plusHours(HOURS_BEFORE_START_EVENT);
        if (startDate.isBefore(munTimePeriod)) {
            throw new IncorrectRequestException("Событие начинается менее чем через " + HOURS_BEFORE_START_EVENT +
                    " часов");
        }
    }

}