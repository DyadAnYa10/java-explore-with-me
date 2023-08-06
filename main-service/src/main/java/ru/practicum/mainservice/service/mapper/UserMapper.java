package ru.practicum.mainservice.service.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.dto.user.UserDto;
import ru.practicum.mainservice.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {
    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public User fromDto(UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public List<UserDto> toDtos(List<User> users) {
        return users.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }
}
