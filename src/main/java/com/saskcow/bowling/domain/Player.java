package com.saskcow.bowling.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private @Id @GeneratedValue Long id;
    private String name;
    @ManyToOne
    private Team team;
    @OneToMany(cascade = CascadeType.ALL)
    private List<PlayerGame> playerGames;

    public Player(String name, Team team) {
        this.name = name;
        this.team = team;
        this.playerGames = new LinkedList<>();
    }

    public Integer getHandicap(){
        if (this.playerGames == null){return 0;}
        List<PlayerGame> last24 = this.playerGames.stream()
                .filter(playerGame -> playerGame.getScores().size() == 3 || playerGame.getScores().size() == 4)
                .collect(Collectors.toList());
        last24 = last24.subList(0, last24.size() >= 24? 24 : last24.size());
        if (last24.size() == 0){return 0;}
        Integer handicap = last24.stream().reduce(0,
                (a, b) -> a + (b.getScores().subList(0, 3).stream()
                        .reduce(0, (c, d) -> c + d.getScratch(), (c, d) -> c + d)/3),
                (a, b) -> a + b)/last24.size();
        handicap = 200 - Math.floorDiv(5 * handicap, 6);
        if (handicap < 0) {
            return 0;
        } else if (handicap > 80) {
            return 80;
        } else {
            return handicap;
        }
    }

    public void addPlayerGame(PlayerGame playerGame) {
        this.playerGames.add(playerGame);
    }

    public void deletePlayerGame(PlayerGame playerGame) {
        this.playerGames.remove(playerGame);
    }
}
