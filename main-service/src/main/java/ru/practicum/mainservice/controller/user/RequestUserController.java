package ru.practicum.mainservice.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.request.RequestDto;
import ru.practicum.mainservice.service.RequestService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class RequestUserController {
    private final RequestService requestService;

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto createRequest(@PathVariable Long userId,
                                    @RequestParam Long eventId) {
        log.info("RequestUserController: Запрос на создание записи пользователя с id={} на событие с id={}",
                userId, eventId);
        return requestService.createRequest(userId, eventId);
    }

    @GetMapping("/{userId}/requests")
    public List<RequestDto> getAllRequests(@PathVariable(name = "userId") Long userId) {
        log.info("RequestUserController: Запрос на получение всех запросов");
        return requestService.getAllRequestsByUserId(userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public RequestDto cancelledRequest(@PathVariable(name = "userId") Long userId,
                                       @PathVariable(name = "requestId") Long requestId) {
        log.info("RequestUserController: Запрос на отмену записи пользователя с id={} на событие с id={}",
                userId, requestId);
        return requestService.cancelledRequestById(userId, requestId);
    }
}
