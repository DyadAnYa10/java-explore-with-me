package ru.practicum.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.dto.compilation.CompilationDto;
import ru.practicum.mainservice.dto.compilation.CompilationNewDto;
import ru.practicum.mainservice.dto.compilation.CompilationUpdateRequest;
import ru.practicum.mainservice.entity.Compilation;
import ru.practicum.mainservice.entity.Event;
import ru.practicum.mainservice.exception.NoFoundObjectException;
import ru.practicum.mainservice.repository.CompilationRepository;
import ru.practicum.mainservice.service.mapper.CompilationMapper;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventPublicService eventService;

    @Transactional
    public CompilationDto createCompilation(CompilationNewDto request) {
        Compilation compilation = CompilationMapper.toCompilation(request);
        if (!Objects.nonNull(request.getPinned())) {
            compilation.setPinned(false);
        }

        if (Objects.nonNull(request.getEvents())) {
            List<Event> getEvent = eventService.getAllEventsByIdIn(request.getEvents());
            compilation.setEvents(getEvent);
        }

        Compilation savedCompilation = compilationRepository.save(compilation);
        return CompilationMapper.toCompilationDto(savedCompilation);
    }

    @Transactional
    public CompilationDto updateCompilationById(Long compilationId, CompilationUpdateRequest request) {
        Compilation foundCompilation = getCompilationByIdIfExist(compilationId);

        if (Objects.nonNull(request.getTitle())) {
            foundCompilation.setTitle(request.getTitle());
        }

        if (Objects.nonNull(request.getPinned())) {
            foundCompilation.setPinned(false);
        }

        if (Objects.nonNull(request.getEvents())) {
            List<Event> foundEvents = eventService.getAllEventsByIdIn(request.getEvents());
            foundCompilation.setEvents(foundEvents);
        }

        Compilation savedCompilation = compilationRepository.save(foundCompilation);
        return CompilationMapper.toCompilationDto(savedCompilation);
    }

    @Transactional
    public void deleteCompilationById(Long compilationId) {
        checkExistCompilationById(compilationId);
        compilationRepository.deleteById(compilationId);
    }

    public List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);

        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pageable);
        return CompilationMapper.toCompilationDtoList(compilations);
    }

    public CompilationDto getCompilationById(Long compilationId) {
        Compilation foundCompilation = getCompilationByIdIfExist(compilationId);
        return CompilationMapper.toCompilationDto(foundCompilation);
    }

    private Compilation getCompilationByIdIfExist(Long compilationId) {
        return compilationRepository.findById(compilationId).orElseThrow(() ->
                new NoFoundObjectException(String.format("Подборка с with id='%s' не найдена", compilationId)));
    }

    private void checkExistCompilationById(Long compilationId) {
        if (compilationRepository.countById(compilationId) == 0) {
            throw new NoFoundObjectException(String.format("Подборка с with id='%s' не найдена", compilationId));
        }
    }
}
