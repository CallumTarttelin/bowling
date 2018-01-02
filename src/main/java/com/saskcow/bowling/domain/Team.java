package com.saskcow.bowling.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Team {
    private @Id @GeneratedValue Long id;
    private String name;
    @OneToMany
    private List<Player> players;
    @OneToMany
    private List<Game> games;
    @ManyToOne
    private League league;

    private Team(){}

    public Team(String name, List<Player> players, List<Game> games, League league) {
        this.name = name;
        this.players = players;
        this.games = games;
        this.league = league;
    }
}
