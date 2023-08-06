package ru.practicum.mainservice.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.service.CommentService;

@Slf4j
@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
public class CommentAdminController {
    private final CommentService commentService;

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByAdmin(@PathVariable Long commentId) {
        log.info("CommentAdminController: запрос на удаление комментария с id='{}'", commentId);
        commentService.deleteComment(commentId);
    }
}