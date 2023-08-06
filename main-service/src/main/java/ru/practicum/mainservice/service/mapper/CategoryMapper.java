package ru.practicum.mainservice.service.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.entity.Category;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CategoryMapper {

    public Category fromDto(CategoryDto categoryDto) {
        return Category.builder()
                .name(categoryDto.getName())
                .build();
    }

    public CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public List<CategoryDto> toDtos(List<Category> categories) {
        return categories.stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
    }
}