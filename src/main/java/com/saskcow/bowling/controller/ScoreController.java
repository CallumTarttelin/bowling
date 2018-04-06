package com.saskcow.bowling.controller;

import com.saskcow.bowling.domain.PlayerGame;
import com.saskcow.bowling.domain.Score;
import com.saskcow.bowling.repository.PlayerGameRepository;
import com.saskcow.bowling.repository.ScoreRepository;
import com.saskcow.bowling.rest.ScoreRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Controller
public class ScoreController {

    private ScoreRepository repo;
    private PlayerGameRepository playerGameRepository;

    @Autowired
    public ScoreController(ScoreRepository repo, PlayerGameRepository playerGameRepository) {

        this.repo = repo;
        this.playerGameRepository = playerGameRepository;
    }

    @RequestMapping(value = "/api/score", method = RequestMethod.POST)
    public ResponseEntity<?> saveScore(@RequestBody ScoreRest score) {
        Optional<PlayerGame> optionalPlayerGame = playerGameRepository.findById(score.getPlayerGameId());
        if (! optionalPlayerGame.isPresent()){
            return ResponseEntity.badRequest().build();
        }
        PlayerGame playerGame = optionalPlayerGame.get();
        Score savedScore = repo.save(new Score(playerGame, score.getScratch(), score.getHandicap() != null ? score.getHandicap() : playerGame.getPlayer().getHandicap()));
        playerGame.addScore(savedScore);
        playerGameRepository.save(playerGame);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedScore.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @RequestMapping(value = "/api/score/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteScore(@PathVariable Long id) {
        Optional<Score> optionalScore = repo.findById(id);
        if (! optionalScore.isPresent()){
            return ResponseEntity.notFound().build();
        }
        Score score = optionalScore.get();
        score.getPlayerGame().deleteScore(score);
        playerGameRepository.save(score.getPlayerGame());
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
