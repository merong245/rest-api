package me.hol22mol22.demorestapi.accounts;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Account {

    @Id @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String email;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER)// 여러개의 enum값을 가질 수 있는 경우
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles;
}
