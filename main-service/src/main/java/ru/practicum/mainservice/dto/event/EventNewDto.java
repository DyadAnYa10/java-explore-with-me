package ru.practicum.mainservice.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.mainservice.dto.location.LocationDto;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EventNewDto {
    @Size(min = 20, max = 2000, message = "Краткое описание должна быть 20 - 2000 символов")
    @NotBlank(message = "Краткое описание не может быть пустым или отсутствовать")
    private String annotation;

    @NotNull(message = "Категория не может быть null")
    @Positive(message = "Id категории должно быть положительным числом")
    private Long category;

    @Size(min = 20, max = 7000, message = "Длина описание должна быть 20 - 2000 символов")
    @NotBlank(message = "Description can not be empty or null")
    private String description;

    @NotNull(message = "Дата события не может быть null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull(message = "Локация не может быть null")
    private LocationDto location;

    private Boolean paid = false;

    @PositiveOrZero
    private Integer participantLimit = 0;

    private Boolean requestModeration = true;

    @Size(min = 3, max = 120, message = "Длина названия должна быть 20 - 2000 символов")
    @NotBlank(message = "Название не может быть пустым или отсутствовать")
    private String title;

}
