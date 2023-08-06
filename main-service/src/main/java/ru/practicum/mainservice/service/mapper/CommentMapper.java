package ru.practicum.mainservice.service.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.entity.Comment;
import ru.practicum.mainservice.entity.Event;
import ru.practicum.mainservice.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CommentMapper {
    public CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .createdOn(comment.getCreatedOn())
                .event(comment.getEvent().getId())
                .text(comment.getText())
                .author(comment.getAuthor().getId())
                .build();
    }

    public Comment fromDto(CommentDto commentDto, Long userId, Long eventId) {
        return Comment.builder()
                .text(commentDto.getText())
                .author(User.builder().id(userId).build())
                .event(Event.builder().id(eventId).build())
                .createdOn(LocalDateTime.now())
                .build();
    }

    public List<CommentDto> toDtos(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
    }
}
