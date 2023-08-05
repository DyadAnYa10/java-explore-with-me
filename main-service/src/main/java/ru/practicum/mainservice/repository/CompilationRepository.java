package ru.practicum.mainservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.mainservice.entity.Compilation;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    Integer countById(Long compilationId);

    @Query("select c from Compilation as c where (:pinned is null or c.pinned = :pinned)")
    List<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);
}
