package com.saskcow.bowling.repository;

import com.saskcow.bowling.BowlingApplication;
import com.saskcow.bowling.domain.Player;
import com.saskcow.bowling.domain.PlayerGame;
import com.saskcow.bowling.domain.Score;
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
public class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;
    private Player swing;

    @Before
    public void populate() {
        Player swing = new Player("Findthee Swing", null);
        swing.addPlayerGame(new PlayerGame(swing, null, null));
        swing.getPlayerGames().get(0).setScores(new LinkedList<>(Arrays.asList(
                new Score(swing.getPlayerGames().get(0), 45, 2),
                new Score(swing.getPlayerGames().get(0), 22, 2),
                new Score(swing.getPlayerGames().get(0), 32, 2),
                new Score(swing.getPlayerGames().get(0), 99, 6, true)
        )));
        swing.addPlayerGame(new PlayerGame(swing, null, null));
        swing.getPlayerGames().get(1).setScores(new LinkedList<>(Arrays.asList(
                new Score(swing.getPlayerGames().get(1), 43, 2),
                new Score(swing.getPlayerGames().get(1), 37, 2),
                new Score(swing.getPlayerGames().get(1), 42, 2),
                new Score(swing.getPlayerGames().get(1), 122, 6, true)
        )));
        this.swing = playerRepository.save(swing);
    }

    @Test
    public void highGame() {
        assertThat(playerRepository.highGame(swing.getId())).isEqualTo(45);
    }

    @Test
    public void highSeries() {
        assertThat(playerRepository.highSeries(swing.getId())).isEqualTo(122);
    }

    @Test
    public void lowGame() {
        assertThat(playerRepository.lowGame(swing.getId())).isEqualTo(22);
    }

    @Test
    public void lowSeries() {
        assertThat(playerRepository.lowSeries(swing.getId())).isEqualTo(99);
    }
}