package me.hol22mol22.demorestapi.events;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import me.hol22mol22.demorestapi.accounts.Account;
import me.hol22mol22.demorestapi.accounts.AccountSerializer;

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
    private EventStatus eventStatus = EventStatus.DRAFT;

    @ManyToOne // event에서만 owner를 참조
    @JsonSerialize(using = AccountSerializer.class)
    private Account manager;

    public void update() {
        // update Free
        if (this.basePrice == 0 && this.maxPrice == 0){
            this.free = true;
        } else{
            this.free = false;
        }

        // update offline
        if (this.location == null || this.location.isBlank()){
            this.offline = false;
        } else{
            this.offline = true;
        }
    }
}
