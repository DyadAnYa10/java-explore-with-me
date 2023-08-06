package ru.practicum.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.mainservice.entity.Request;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequesterId(Long userId);

    Optional<Request> findByIdAndRequesterId(Long requestId, Long userId);

    List<Request> findAllByEventId(Long eventId);

    List<Request> findByRequesterIdAndEventId(Long userId, Long eventId);

    Optional<Request> findByEventIdAndRequesterId(Long eventId, Long userId);
}
