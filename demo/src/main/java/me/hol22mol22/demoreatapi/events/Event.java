package me.hol22mol22.demoreatapi.events;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "id")
@Entity
public class Event {

    @Id @GeneratedValue
    private Integer id;

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

    private boolean offline;
    private boolean free;
    @Enumerated(EnumType.STRING) // 기본값 ORDINAL -> 구조 바뀌면 전부 바뀜
    private EventStatus eventStatus;
}
