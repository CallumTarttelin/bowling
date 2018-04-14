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
import java.util.Arrays;
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

        Optional<Player> optionalPlayer0 = playerRepository.findById(playerGameRest.getPlayerIds().get(0));
        Optional<Player> optionalPlayer1 = playerRepository.findById(playerGameRest.getPlayerIds().get(1));
        Optional<Player> optionalPlayer2 = playerRepository.findById(playerGameRest.getPlayerIds().get(2));
        Optional<Team> optionalTeam = teamRepository.findById(playerGameRest.getTeamId());
        Optional<Game> optionalGame = gameRepository.findById(playerGameRest.getGameId());
        if (! optionalGame.isPresent() || ! optionalPlayer0.isPresent() || ! optionalPlayer1.isPresent() || ! optionalPlayer2.isPresent() || ! optionalTeam.isPresent()){
            return ResponseEntity.badRequest().build();
        }
        Player player0 = optionalPlayer0.get();
        Player player1 = optionalPlayer1.get();
        Player player2 = optionalPlayer2.get();
        Team team = optionalTeam.get();
        Game game = optionalGame.get();
        if(game.getPlayerGames().size() > 3) {
            return ResponseEntity.badRequest().build();
        }

        PlayerGame playerGame0 = repo.save(new PlayerGame(player0, team, game));
        PlayerGame playerGame1 = repo.save(new PlayerGame(player1, team, game));
        PlayerGame playerGame2 = repo.save(new PlayerGame(player2, team, game));

        player0.addPlayerGame(playerGame0);
        player1.addPlayerGame(playerGame1);
        player2.addPlayerGame(playerGame2);
        team.addAllPlayerGame(Arrays.asList(playerGame0, playerGame1, playerGame2));
        game.addAllPlayerGame(Arrays.asList(playerGame0, playerGame1, playerGame2));

        playerRepository.save(player0);
        playerRepository.save(player1);
        playerRepository.save(player2);
        teamRepository.save(team);
        gameRepository.save(game);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(game.getId()).toUri();

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
