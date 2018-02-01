package com.saskcow.bowling.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Team {
    private @Id @GeneratedValue Long id;
    private String name;
    @OneToMany
    private List<Player> players;
    @OneToMany
    private List<Game> games;
    @ManyToOne
    private League league;

    public Team(String name, List<Player> players, List<Game> games, League league) {
        this.name = name;
        this.players = players;
        this.games = games;
        this.league = league;
    }

    public Team(String name, League league) {
        this.name = name;
        this.league = league;
    }
}
