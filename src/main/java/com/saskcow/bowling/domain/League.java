package com.saskcow.bowling.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class League {
    private @Id @GeneratedValue Long id;
    private String name;
    @OneToMany
    private List<Game> games;
    @OneToMany
    private List<Team> teams;

    public League(String name, List<Game> games, List<Team> teams){
        this.name = name;
        this.games = games;
        this.teams = teams;
    }

}
