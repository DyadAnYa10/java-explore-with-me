package ru.practicum.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.entity.Comment;
import ru.practicum.mainservice.entity.Event;
import ru.practicum.mainservice.entity.Request;
import ru.practicum.mainservice.entity.User;
import ru.practicum.mainservice.exception.AccessException;
import ru.practicum.mainservice.exception.EventConflictException;
import ru.practicum.mainservice.exception.NoFoundObjectException;
import ru.practicum.mainservice.model.EventState;
import ru.practicum.mainservice.model.RequestStatus;
import ru.practicum.mainservice.repository.CommentRepository;
import ru.practicum.mainservice.service.mapper.CommentMapper;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final EventService eventService;
    private final RequestService requestService;

    @Transactional
    public CommentDto createComment(Long userId, Long eventId, CommentDto commentDto) {
        User user = userService.getUserByIdIfExist(userId);

        Event event = eventService.getEventByIdIfExist(eventId);

        if (!Objects.equals(event.getState(), EventState.PUBLISHED)) {
            throw new EventConflictException("Статус события должен быть 'PUBLISHED'");
        }

        Optional<Request> optionalRequest = requestService.getRequestByUserIdAndEventId(userId, eventId);

        if (!Objects.equals(user.getId(), event.getInitiator().getId()) && (optionalRequest.isEmpty()
                || (!Objects.equals(optionalRequest.get().getStatus(), RequestStatus.CONFIRMED)))) {
            throw new AccessException(String.format("Пользователь с id='%s' не участвовал в событии с id='%s' " +
                    "и не может оставить комментарий", userId, eventId));
        }


        Optional<Comment> foundComment = commentRepository.findByEventIdAndAuthorId(eventId, userId);
        if (foundComment.isPresent()) {
            throw new AccessException(String.format("Пользователь с id='%s' уже оставлял комментарий к событию " +
                    "с id='%s'", userId, eventId));
        }

        Comment comment = CommentMapper.fromDto(commentDto, userId, eventId);

        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.toDto(savedComment);
    }


    public void deleteCommentById(Long commentId, Long userId) {
        Comment comment = getCommentByIdIfExist(commentId);

        checkUserIsAuthorComment(comment.getAuthor().getId(), userId, commentId);

        commentRepository.deleteById(commentId);
    }


    public void deleteComment(Long commentId) {
        checkExistCommentById(commentId);
        commentRepository.deleteById(commentId);
    }

    @Transactional
    public CommentDto updateCommentById(Long commentId, Long userId, CommentDto commentDto) {
        Comment foundComment = getCommentByIdIfExist(commentId);

        checkUserIsAuthorComment(foundComment.getAuthor().getId(), userId, commentId);

        String newText = commentDto.getText();
        if (StringUtils.hasLength(newText)) {
            foundComment.setText(newText);
        }

        Comment savedComment = commentRepository.save(foundComment);
        return CommentMapper.toDto(savedComment);
    }

    public List<CommentDto> getAllCommentsByEventId(Long eventId, Integer from, Integer size) {
        eventService.checkExistEventById(eventId);

        PageRequest pageRequest = PageRequest.of(from, size);
        List<Comment> comments = commentRepository.findAllByEventIdOrderByCreatedOnDesc(eventId, pageRequest);

        return CommentMapper.toDtos(comments);
    }

    private void checkUserIsAuthorComment(Long authorId, Long userId, Long commentId) {
        if (!Objects.equals(authorId, userId)) {
            throw new AccessException(String.format(
                    "Пользователь с id='%s' не является автором комментария с id='%s' и не может его удалить / изменить",
                    userId, commentId));
        }
    }

    private Comment getCommentByIdIfExist(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new NoFoundObjectException(String.format("Комментарий с id='%s' не найден", commentId)));
    }

    private void checkExistCommentById(Long commentId) {
        if (commentRepository.countById(commentId) <= 0) {
            throw new NoFoundObjectException(String.format("Комментарий с id='%s' не найден", commentId));
        }
    }
}