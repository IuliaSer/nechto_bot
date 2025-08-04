package nechto.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import nechto.enums.Status;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SCORES")
@Accessors(chain = true)
public class Scores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    private Game game;

    private float scores;

    @ElementCollection(targetClass = Status.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "SCORES_STATUS", joinColumns = @JoinColumn(name = "scores_id"))
    @Column(name = "status_id")
    private List<Status> statuses;
}
