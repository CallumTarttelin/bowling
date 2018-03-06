package com.saskcow.bowling.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Rota {
    private @Id @GeneratedValue Long id;
    @OneToOne
    private League league;
    @OneToMany(mappedBy = "rota", cascade = CascadeType.ALL)
    private List<Game> games;
    private LocalDateTime start;
    private LocalDateTime end;
    private Integer weeklyGames;

    public Rota (League league) {
        this.league = league;
        this.games = new LinkedList<>();
    }

    public Rota (League league, LocalDateTime start, LocalDateTime end, Integer weeklyGames){
        this.league = league;
        this.start = start;
        this.end = end;
        this.weeklyGames = weeklyGames;
        this.games = new LinkedList<>();
    }

    public void addGame(Game game) {
        this.games.add(game);
    }

    public void deleteGame(Game game) {
        this.games.remove(game);
    }
}
