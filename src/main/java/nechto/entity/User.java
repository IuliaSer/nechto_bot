package nechto.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import nechto.enums.Authority;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USERS")
@Accessors(chain = true)
public class User {

    @Id
    private Long id;

    private String name;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Scores> scores;
}
