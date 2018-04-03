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
public class Player {
    private @Id @GeneratedValue Long id;
    private String name;
    @ManyToOne
    private Team team;
    @OneToMany
    private List<PlayerGame> playerGames;

    public Player(String name, Team team) {
        this.name = name;
        this.team = team;
        this.playerGames = new LinkedList<>();
    }

    public Integer getHandicap(){
        return 2;
    }
}
