package com.saskcow.bowling.controller;

import com.saskcow.bowling.domain.Game;
import com.saskcow.bowling.domain.Player;
import com.saskcow.bowling.domain.PlayerGame;
import com.saskcow.bowling.domain.Team;
import com.saskcow.bowling.repository.GameRepository;
import com.saskcow.bowling.repository.PlayerGameRepository;
import com.saskcow.bowling.repository.PlayerRepository;
import com.saskcow.bowling.repository.TeamRepository;
import com.saskcow.bowling.rest.PlayerGameRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Controller
public class PlayerGameController {

    private PlayerGameRepository repo;
    private PlayerRepository playerRepository;
    private TeamRepository teamRepository;
    private GameRepository gameRepository;

    @Autowired
    public PlayerGameController(PlayerGameRepository repo, PlayerRepository playerRepository, TeamRepository teamRepository, GameRepository gameRepository){
        this.repo = repo;
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.gameRepository = gameRepository;
    }

    @PostMapping("/api/playergame")
    public ResponseEntity<?> savePlayerGame(@RequestBody PlayerGameRest playerGameRest) {

        Optional<Player> optionalPlayer = playerRepository.findById(playerGameRest.getPlayerId());
        Optional<Team> optionalTeam = teamRepository.findById(playerGameRest.getTeamId());
        Optional<Game> optionalGame = gameRepository.findById(playerGameRest.getGameId());

        if (! optionalGame.isPresent() || ! optionalPlayer.isPresent() || ! optionalTeam.isPresent()){
            return ResponseEntity.notFound().build();
        }
        Player player = optionalPlayer.get();
        Team team = optionalTeam.get();
        Game game = optionalGame.get();

        PlayerGame playerGame = repo.save(new PlayerGame(player, team, game));

        player.addPlayerGame(playerGame);
        team.addPlayerGame(playerGame);
        game.addPlayerGame(playerGame);

        playerRepository.save(player);
        teamRepository.save(team);
        gameRepository.save(game);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(playerGame.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/api/playergame/{id}")
    public ResponseEntity<Void> deletePlayerGame(@PathVariable Long id) {
        Optional<PlayerGame> optionalPlayerGame = repo.findById(id);
        if (! optionalPlayerGame.isPresent()){
            return ResponseEntity.notFound().build();
        }
        PlayerGame playerGame = optionalPlayerGame.get();

        playerGame.getPlayer().deletePlayerGame(playerGame);
        playerGame.getTeam().deletePlayerGame(playerGame);
        playerGame.getGame().deletePlayerGame(playerGame);

        playerRepository.save(playerGame.getPlayer());
        teamRepository.save(playerGame.getTeam());
        gameRepository.save(playerGame.getGame());

        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
