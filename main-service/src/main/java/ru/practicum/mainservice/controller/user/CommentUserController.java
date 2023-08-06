package ru.practicum.mainservice.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.service.CommentService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
public class CommentUserController {
    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable Long userId,
                                    @RequestParam Long eventId,
                                    @Valid @RequestBody CommentDto commentDto) {
        log.info("CommentUserController: запрос пользователя с id='{}' на создание комментария {} к событию с id='{}'",
                userId, commentDto, eventId);
        return commentService.createComment(userId, eventId, commentDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long commentId) {
        log.info("CommentUserController: запрос пользователя с id='{}' на удаление комментария с id='{}'",
                userId, commentId);
        commentService.deleteCommentById(commentId, userId);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@PathVariable Long userId,
                                    @PathVariable Long commentId,
                                    @Valid @RequestBody CommentDto commentDto) {
        log.info("CommentUserController: запрос пользователя с id='{}' на изменение комментария с id='{}', " +
                        "новый комментарий: {}", userId, commentId, commentDto);
        return commentService.updateCommentById(commentId, userId, commentDto);
    }
}
