package ru.practicum.mainservice.dto.event;

import lombok.*;
import ru.practicum.mainservice.model.RequestStatus;

import java.util.Set;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateRequest {
    private Set<Long> requestIds;
    private RequestStatus status;
}
