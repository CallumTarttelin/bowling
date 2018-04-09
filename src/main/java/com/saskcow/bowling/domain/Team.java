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
public class Team {
    private @Id @GeneratedValue Long id;
    private String name;
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<Player> players;
    @ManyToMany
    private List<Game> games;
    @ManyToOne
    private League league;
    @OneToMany
    private List<PlayerGame> playerGames;

    public Team(String name, List<Player> players, List<Game> games, League league) {
        this.name = name;
        this.players = players;
        this.games = games;
        this.league = league;
    }

    public Team(String name, League league) {
        this.name = name;
        this.league = league;
        this.players = new LinkedList<>();
        this.games = new LinkedList<>();
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void deletePlayer(Player player) {
        this.players.remove(player);
    }

    public void addGame(Game game) {
        this.games.add(game);
    }

    public void deleteGame(Game game) {
        this.games.remove(game);
    }

    public void addPlayerGame(PlayerGame playerGame) {
        this.playerGames.add(playerGame);
    }

    public void addAllPlayerGame(List<PlayerGame> playerGames) {
        this.playerGames.addAll(playerGames);
    }

    public void deletePlayerGame(PlayerGame playerGame) {
        this.playerGames.remove(playerGame);
    }
}
