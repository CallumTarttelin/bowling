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
    @OneToOne(mappedBy = "league", cascade = CascadeType.ALL)
    private Rota rota;
    @OneToMany(mappedBy = "league", cascade = CascadeType.ALL)
    private List<Team> teams;

    public League(String name, Rota rota, List<Team> teams){
        this.name = name;
        this.teams = teams;
        this.rota = rota;
    }

    public void addTeam(Team team) {
        this.teams.add(team);
    }

    public void deleteTeam(Team team) {
        this.teams.remove(team);
    }

    public List<Game> getGames() {
        return rota.getGames();
    }
}
