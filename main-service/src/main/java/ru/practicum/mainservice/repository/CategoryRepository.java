package ru.practicum.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.mainservice.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
