package nechto.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;
import nechto.enums.Authority;
import org.hibernate.annotations.DynamicInsert;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USERS")
@Accessors(chain = true)
@DynamicInsert
public class User {

    @Id
    private Long id;

    @NotNull(message = "Name should not be null")
    @Size(min = 3, max = 10, message = "Username should have expected size")
    private String name;

    @NotNull(message = "Username should not be null")
    @Size(min = 3, max = 10, message = "Username should have expected size")
    private String username;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Authority authority = Authority.ROLE_USER;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Scores> scores;
}
