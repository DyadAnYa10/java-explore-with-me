package ru.practicum.mainservice.service.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.dto.request.RequestDto;
import ru.practicum.mainservice.entity.Request;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class RequestMapper {
    public RequestDto toDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .created(request.getCreated())
                .requester(request.getId())
                .status(request.getStatus())
                .build();
    }

    public  List<RequestDto> toDtos(List<Request> requests) {
        return requests.stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }
}
