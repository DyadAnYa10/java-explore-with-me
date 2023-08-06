package ru.practicum.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.entity.Event;
import ru.practicum.mainservice.exception.IncorrectRequestException;
import ru.practicum.mainservice.exception.NoFoundObjectException;
import ru.practicum.mainservice.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public void checkExistEventById(Long eventId) {
        eventRepository.findById(eventId).orElseThrow(() ->
                new NoFoundObjectException(String.format("Событие с id='%s' не найдено", eventId)));
    }

    public Event getEventByIdIfExist(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NoFoundObjectException(String.format("Событие с id='%s' не найдено", eventId)));
    }

    public void checkEndIsAfterStart(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IncorrectRequestException("Дата начала не может быть позже даты конца");
        }
    }

    public List<Event> getAllEventsByIdIn(Set<Long> events) {
        return eventRepository.findAllByIdIn(events);
    }
}
