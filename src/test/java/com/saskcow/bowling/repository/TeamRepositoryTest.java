package com.saskcow.bowling.repository;

import com.saskcow.bowling.BowlingApplication;
import com.saskcow.bowling.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.LinkedList;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {BowlingApplication.class})
@ActiveProfiles("test")
public class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private GameRepository gameRepository;
    private Team cableStreet;

    @Before
    public void populate() {
        Team cableStreet = new Team("Cable Street Particulars", null);
        cableStreet.addPlayer(new Player("Findthee Swing", cableStreet));
        cableStreet.addPlayer(new Player("Carcer", cableStreet));
        cableStreet.addPlayer(new Player("Todzy", cableStreet));
        this.cableStreet = teamRepository.save(cableStreet);

        Team nightWatch = new Team("The Night Watch", null);
        nightWatch.addPlayer(new Player("Sam Vimes", nightWatch));
        nightWatch.addPlayer(new Player("Nobby Nobbs", nightWatch));
        nightWatch.addPlayer(new Player("Carrot Ironfoundersson", nightWatch));
        teamRepository.save(nightWatch);

        Game game1 = new Game(null, null, "", new LinkedList<>(Arrays.asList(nightWatch, cableStreet)));
        Game game2 = new Game(null, null, "", new LinkedList<>(Arrays.asList(nightWatch, cableStreet)));

        cableStreet.addGame(game1);
        cableStreet.addGame(game2);
        nightWatch.addGame(game1);
        nightWatch.addGame(game2);

        gameRepository.save(game1);
        gameRepository.save(game2);

        cableStreet.setPlayerGames(new LinkedList<>());
        nightWatch.setPlayerGames(new LinkedList<>());
        game1.setPlayerGames(new LinkedList<>());
        game2.setPlayerGames(new LinkedList<>());

        nightWatch.getPlayers().forEach(player -> {
            player.addPlayerGame(new PlayerGame(player, nightWatch, game1));
            player.addPlayerGame(new PlayerGame(player, nightWatch, game2));
            game1.addPlayerGame(player.getPlayerGames().get(0));
            game2.addPlayerGame(player.getPlayerGames().get(1));
            nightWatch.addAllPlayerGame(player.getPlayerGames());
        });

        cableStreet.getPlayers().forEach(player -> {
            player.addPlayerGame(new PlayerGame(player, cableStreet, game1));
            player.addPlayerGame(new PlayerGame(player, cableStreet, game2));
            game1.addPlayerGame(player.getPlayerGames().get(0));
            game2.addPlayerGame(player.getPlayerGames().get(1));
            cableStreet.addAllPlayerGame(player.getPlayerGames());
        });

        cableStreet.getPlayerGames().stream().filter(playerGame -> playerGame.getGame() == game1).forEach(playerGame -> {
            playerGame.addScore(new Score(playerGame, 50));
            playerGame.addScore(new Score(playerGame, 100));
            playerGame.addScore(new Score(playerGame, 150));
        });

        cableStreet.getPlayerGames().stream().filter(playerGame -> playerGame.getGame() == game2).forEach(playerGame -> {
            playerGame.addScore(new Score(playerGame, 100));
            playerGame.addScore(new Score(playerGame, 150));
            playerGame.addScore(new Score(playerGame, 200));
        });

        nightWatch.getPlayerGames().forEach(playerGame -> {
            playerGame.addScore(new Score(playerGame, 100));
            playerGame.addScore(new Score(playerGame, 150));
            playerGame.addScore(new Score(playerGame, 100));
        });

        game1.addPlayerGame(3, new PlayerGame(null, game1.getTeams().get(0), game1));
        game1.addPlayerGame(new PlayerGame(null, game1.getTeams().get(1), game1));
        game1.completeGame();

        game2.addPlayerGame(3, new PlayerGame(null, game2.getTeams().get(0), game2));
        game2.addPlayerGame(new PlayerGame(null, game2.getTeams().get(1), game2));
        game2.completeGame();

        gameRepository.save(game1);
        gameRepository.save(game2);

        this.cableStreet = teamRepository.save(cableStreet);

    }

    @Test
    public void highHandicapGame() {
        assertThat(teamRepository.highHandicapGame(this.cableStreet.getId())).isEqualTo(600);
    }

    @Test
    public void highHandicapSeries() {
        assertThat(teamRepository.highHandicapSeries(this.cableStreet.getId())).isEqualTo(1350);
    }

    @Test
    public void pinsFor() {
        assertThat(teamRepository.pinsFor(cableStreet.getId())).isEqualTo(2250);
    }

    @Test
    public void pinsAgainst() {
        assertThat(teamRepository.pinsAgainst(cableStreet.getId())).isEqualTo(2100);
    }

    @Test
    public void teamPoints() {
        assertThat(teamRepository.teamPoints(cableStreet.getId())).isEqualTo(8);
    }

    @Test
    public void totalPoints() {
        assertThat(teamRepository.totalPoints(cableStreet.getId())).isEqualTo(32);
    }
}