package me.hol22mol22.demoreatapi.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder @NoArgsConstructor @AllArgsConstructor
@Data
public class EventDto {

    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;

    // Optional
    private String location;
    private int basePrice;
    private int maxPrice;
    private int limitOfEnrollment;
}
