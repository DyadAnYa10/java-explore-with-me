package ru.practicum.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.mainservice.dto.event.EventFullDto;
import ru.practicum.mainservice.dto.event.EventUpdateAdminRequest;
import ru.practicum.mainservice.entity.Category;
import ru.practicum.mainservice.entity.Event;
import ru.practicum.mainservice.exception.EventConflictException;
import ru.practicum.mainservice.exception.IncorrectRequestException;
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
public class EventAdminService {
    private static final Integer HOURS_BEFORE_START_EVENT = 1;
    private final EventRepository eventRepository;
    private final CategoryService categoryService;
    private final EventService generalEventService;

    public List<EventFullDto> getAllEvents(List<Long> users, List<String> states,
                                           List<Long> categories, LocalDateTime startDate, LocalDateTime endDate,
                                           Integer from, Integer size) {
        generalEventService.checkEndIsAfterStart(startDate, endDate);

        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));

        Specification<Event> specification = Specification.where(null);

        if (Objects.nonNull(users) && !users.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("initiator").get("id").in(users));
        }
        if (Objects.nonNull(states) && !states.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("state").as(String.class).in(states));
        }
        if (Objects.nonNull(categories) && !categories.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("category").get("id").in(categories));
        }
        if (Objects.nonNull(startDate)) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), startDate));
        }
        if (Objects.nonNull(endDate)) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), endDate));
        }

        List<Event> events = eventRepository.findAll(specification, pageable);
        return EventMapper.toFullDtos(events);
    }

    @Transactional
    public EventFullDto updateEventById(Long eventId, EventUpdateAdminRequest request) {
        Event foundEvent = generalEventService.getEventByIdIfExist(eventId);

        if (!Objects.equals(EventState.PENDING, foundEvent.getState())) {
            throw new EventConflictException("Статус события должен быть 'PENDING'");
        }

        if (Objects.nonNull(request.getAnnotation()) && StringUtils.hasLength(request.getAnnotation())) {
            foundEvent.setAnnotation(request.getAnnotation());
        }
        if (Objects.nonNull(request.getTitle()) && StringUtils.hasLength(request.getTitle())) {
            foundEvent.setTitle(request.getTitle());
        }
        if (Objects.nonNull(request.getCategory())) {
            final Category category = categoryService.getCategoryByIdIfExist(request.getCategory());
            foundEvent.setCategory(category);
        }
        if (Objects.nonNull(request.getDescription()) && StringUtils.hasLength(request.getDescription())) {
            foundEvent.setDescription(request.getDescription());
        }
        if (Objects.nonNull(request.getEventDate())) {
            checksStartTimeAfterMinPeriod(request.getEventDate());
            foundEvent.setEventDate(request.getEventDate());
        }
        if (Objects.nonNull(request.getLocation())) {
            foundEvent.setLocation(LocationMapper.fromDto(request.getLocation()));
        }
        if (Objects.nonNull(request.getPaid())) {
            foundEvent.setPaid(request.getPaid());
        }
        if (Objects.nonNull(request.getParticipantLimit())) {
            foundEvent.setParticipantLimit(request.getParticipantLimit());
        }
        if (Objects.nonNull(request.getRequestModeration())) {
            foundEvent.setRequestModeration(request.getRequestModeration());
        }
        if (Objects.nonNull(request.getStateAction())) {
            switch (request.getStateAction()) {
                case PUBLISH_EVENT:
                    foundEvent.setState(EventState.PUBLISHED);
                    break;
                case REJECT_EVENT:
                    foundEvent.setState(EventState.CANCELED);
                    break;
            }
        }
        Event updatedEvent = eventRepository.save(foundEvent);
        return EventMapper.toFullDto(updatedEvent);
    }

    private void checksStartTimeAfterMinPeriod(LocalDateTime startDate) {
        LocalDateTime minStartDate = LocalDateTime.now().plusHours(HOURS_BEFORE_START_EVENT);
        if (startDate.isBefore(minStartDate)) {
            throw new IncorrectRequestException("До начала события менее " + HOURS_BEFORE_START_EVENT + " часов");
        }
    }
}