package nechto.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@jakarta.persistence.Table(name = "TABLE")
public class Table {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameOrNumber;

    @JoinColumn(name = "admin_id", referencedColumnName = "id")
    private User admin;

    private LocalDateTime date;

    @Fetch(FetchMode.JOIN)
    @ManyToMany(mappedBy = "table", cascade = CascadeType.ALL)
    private List<User> currentUsers;

    @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL)
    private List<Game> games;
}
