package com.saskcow.bowling.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Player {
    private @Id @GeneratedValue Long id;
    private String name;
    @OneToMany
    private List<Score> scores;
    @ManyToOne
    private Team team;
    private Integer handicap;

    private Player() {}

    public Player(String name, List<Score> scores, Team team, Integer handicap) {
        this.name = name;
        this.scores = scores;
        this.team = team;
        this.handicap = handicap;
    }
}
