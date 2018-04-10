package com.saskcow.bowling.controller;

import com.saskcow.bowling.domain.Game;
import com.saskcow.bowling.domain.PlayerGame;
import com.saskcow.bowling.domain.Rota;
import com.saskcow.bowling.domain.Team;
import com.saskcow.bowling.repository.GameRepository;
import com.saskcow.bowling.repository.PlayerGameRepository;
import com.saskcow.bowling.repository.RotaRepository;
import com.saskcow.bowling.repository.TeamRepository;
import com.saskcow.bowling.rest.GameRest;
import com.saskcow.bowling.view.GameView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Controller
public class GameController {
    private GameRepository repo;
    private TeamRepository teamRepository;
    private RotaRepository rotaRepository;

    @Autowired
    public GameController(GameRepository repo, TeamRepository teamRepository, RotaRepository rotaRepository){
        this.repo = repo;
        this.teamRepository = teamRepository;
        this.rotaRepository = rotaRepository;
    }

    @RequestMapping(value = "/api/game/{id}", method = RequestMethod.GET)
    public ResponseEntity<GameView> findGame(@PathVariable("id") Long id) {
        Optional<Game> optionalGame = repo.findById(id);
        if (! optionalGame.isPresent()){
            return ResponseEntity.notFound().build();
        }
        Game game = optionalGame.get();
        GameView gameView = new GameView(game);
        return ResponseEntity.ok(gameView);
    }

    @RequestMapping(value = "/api/game/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> saveGame(@PathVariable Long id) {
        Optional<Game> optionalGame = repo.findById(id);
        if (! optionalGame.isPresent()){
            return ResponseEntity.notFound().build();
        }
        Game game = optionalGame.get();
        if (game.getPlayerGames().size() != 6 || ! game.getPlayerGames().stream().allMatch(playerGame -> playerGame.getScores().size() == 3)){
            return ResponseEntity.badRequest().build();
        }
        game.addPlayerGame(3, new PlayerGame(null, game.getTeams().get(0), game));
        game.addPlayerGame(new PlayerGame(null, game.getTeams().get(1), game));
        game.completeGame();
        repo.save(game);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/api/game", method = RequestMethod.POST)
    public ResponseEntity<?> saveGame(@RequestBody GameRest game) {
        Optional<Team> optionalTeam1 = teamRepository.findById(game.getTeamId1());
        Optional<Team> optionalTeam2 = teamRepository.findById(game.getTeamId2());
        Optional<Rota> optionalRota = rotaRepository.findById(game.getRotaId());
        if (! optionalRota.isPresent() ||! optionalTeam2.isPresent() || ! optionalTeam1.isPresent()){
            return ResponseEntity.badRequest().build();
        }
        Team team1 = optionalTeam1.get();
        Team team2 = optionalTeam2.get();
        Rota rota = optionalRota.get();
        List<Team> teams = new LinkedList<>(Arrays.asList(team1, team2));
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime dateTime = LocalDateTime.parse(game.getTime(), formatter);
        Game savedGame = repo.save(new Game(rota, dateTime, game.getVenue(), teams));
        team1.addGame(savedGame);
        team2.addGame(savedGame);
        rota.addGame(savedGame);
        teamRepository.save(team1);
        teamRepository.save(team2);
        rotaRepository.save(rota);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedGame.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @RequestMapping(value = "/api/game/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteGame(@PathVariable("id") Long id) {
        Optional<Game> optionalGame = repo.findById(id);
        if (! optionalGame.isPresent()){
            return ResponseEntity.badRequest().build();
        }
        Game game = optionalGame.get();
        game.getTeams().forEach(team -> team.deleteGame(game));
        game.getRota().deleteGame(game);
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
