package ru.practicum.mainservice.dto.location;

import lombok.*;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    @NotNull(message = "Широта не может быть пустой или отсутствовать")
    private Float lat;

    @NotNull(message = "Долгота не может быть пустой или отсутствовать")
    private Float lon;
}