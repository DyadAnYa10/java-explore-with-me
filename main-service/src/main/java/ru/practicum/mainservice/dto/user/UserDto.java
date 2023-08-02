package ru.practicum.mainservice.dto.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(message = "Email не может быть пустым или отсутствовать")
    @Email(message = "Email неправильного формата (email@email.com)")
    @Size(min = 6, max = 254)
    private String email;

    @Size(min = 2, max = 250, message = "Длина имени должна быть 2 - 250 символов")
    @NotBlank(message = "Имя не может быть пустым или отсутствовать")
    private String name;
}
