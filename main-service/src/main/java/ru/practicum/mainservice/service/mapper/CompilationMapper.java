package ru.practicum.mainservice.service.mapper;


import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.dto.compilation.CompilationDto;
import ru.practicum.mainservice.dto.compilation.CompilationNewDto;
import ru.practicum.mainservice.dto.compilation.CompilationUpdateRequest;
import ru.practicum.mainservice.entity.Compilation;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {
    public CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(EventMapper.toEventShortDtoList(compilation.getEvents()))
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

    public Compilation toCompilation(CompilationNewDto dto) {
        return Compilation.builder()
                .pinned(dto.getPinned())
                .title(dto.getTitle())
                .events(List.of())
                .build();
    }

    public Compilation toCompilation(CompilationUpdateRequest dto) {
        return Compilation.builder()
                .id(dto.getId())
                .pinned(dto.getPinned())
                .title(dto.getTitle())
                .build();
    }

    public List<CompilationDto> toCompilationDtoList(List<Compilation> compilations) {
        return compilations.stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }
}
