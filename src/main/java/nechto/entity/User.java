package nechto.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    private String name;

    private String username;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Authority authority = Authority.ROLE_USER;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Scores> scores;
}
