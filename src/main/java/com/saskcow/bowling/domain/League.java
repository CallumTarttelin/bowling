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
public class League {
    private @Id @GeneratedValue Long id;
    private String name;
    @OneToMany(mappedBy = "league", cascade = CascadeType.ALL)
    private List<Game> games;
    @OneToMany(mappedBy = "league", cascade = CascadeType.ALL)
    private List<Team> teams;

    public League(String name, List<Game> games, List<Team> teams){
        this.name = name;
        this.games = games;
        this.teams = teams;
    }

    public void addTeam(Team team) {
        this.teams.add(team);
    }

    public void deleteTeam(Team team) {
        this.teams.remove(team);
    }
}
