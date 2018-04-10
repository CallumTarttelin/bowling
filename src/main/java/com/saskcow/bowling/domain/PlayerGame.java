package com.saskcow.bowling.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PlayerGame {
    private @GeneratedValue @Id Long id;
    @ManyToOne
    private Player player;
    @ManyToOne
    private Team team;
    @ManyToOne
    private Game game;
    @OneToMany(mappedBy = "playerGame", cascade = CascadeType.ALL)
    private List<Score> scores;

    public PlayerGame(Player player, Team team, Game game) {
        this.player = player;
        this.team = team;
        this.game = game;
        this.scores = new LinkedList<>();
    }

    public void addScore(Score score) {
        this.scores.add(score);
    }

    public void deleteScore(Score score) {
        this.scores.remove(score);
    }

    public Integer getPoints() {
        return this.scores.stream().reduce(0, (a, b) -> a + b.getScore(), (a, b) -> a + b);
    }
}
