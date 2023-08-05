package ru.practicum.mainservice.dto.event;

import lombok.*;
import ru.practicum.mainservice.dto.request.RequestDto;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateResponse {
    private List<RequestDto> confirmedRequests;
    private List<RequestDto> rejectedRequests;
}
