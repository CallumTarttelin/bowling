package com.saskcow.bowling.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class League {
    private @Id @GeneratedValue Long id;
    private String name;
    @OneToOne
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
        List<List<Game>> listGames = this.teams == null ? new LinkedList<>() : this.teams.stream().map(Team::getGames).collect(Collectors.toList());
        List<Game> games = listGames.stream().flatMap(List::stream).collect(Collectors.toList());
        return new LinkedList<>(new LinkedHashSet<>(games));
    }
}
