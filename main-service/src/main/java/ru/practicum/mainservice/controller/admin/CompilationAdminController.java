package ru.practicum.mainservice.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.compilation.CompilationDto;
import ru.practicum.mainservice.dto.compilation.CompilationNewDto;
import ru.practicum.mainservice.dto.compilation.CompilationUpdateRequest;
import ru.practicum.mainservice.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@RequestBody @Valid CompilationNewDto request) {
        log.info("CompilationAdminController: Запрос на создание новой подборки {}", request);
        return compilationService.createCompilation(request);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@PathVariable(name = "compId") @Positive Long compilationId,
                                            @RequestBody @Valid CompilationUpdateRequest request) {
        log.info("CompilationAdminController: Request to update compilation with id='{}', new parameters={}",
                compilationId, request);
        return compilationService.updateCompilationById(compilationId, request);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable(name = "compId") @Positive Long compilationId) {
        log.info("CompilationAdminController: Request to delete compilation with id='{}'", compilationId);
        compilationService.deleteCompilationById(compilationId);
    }
}