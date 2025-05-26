package nechto.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@NamedEntityGraph(name = "game_entity_graph", attributeNodes = @NamedAttributeNode("users"))
//@NamedEntityGraph(
//        name = "game_with_users_scores_statuses",
//        attributeNodes = {
//                @NamedAttributeNode("users"),
//                @NamedAttributeNode(value = "scores", subgraph = "scores-with-statuses")
//        },
//        subgraphs = {
//                @NamedSubgraph(name = "scores-with-statuses", attributeNodes = {
//                        @NamedAttributeNode("statuses")
//                })
//        }
//)
@Table(name = "GAMES")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    @ManyToMany(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinTable(name = "SCORES", joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;

//    @OneToMany(mappedBy = "game", fetch = FetchType.LAZY, cascade = {})
//    private List<Scores> scores;

}
