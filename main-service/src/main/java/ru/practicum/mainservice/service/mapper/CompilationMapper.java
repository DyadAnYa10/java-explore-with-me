package ru.practicum.mainservice.service.mapper;


import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.dto.compilation.CompilationDto;
import ru.practicum.mainservice.dto.compilation.CompilationNewDto;
import ru.practicum.mainservice.entity.Compilation;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {
    public CompilationDto toDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(EventMapper.toShortDtos(compilation.getEvents()))
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

    public Compilation fromDto(CompilationNewDto dto) {
        return Compilation.builder()
                .pinned(dto.getPinned())
                .title(dto.getTitle())
                .events(List.of())
                .build();
    }

    public List<CompilationDto> toDtos(List<Compilation> compilations) {
        return compilations.stream()
                .map(CompilationMapper::toDto)
                .collect(Collectors.toList());
    }
}
