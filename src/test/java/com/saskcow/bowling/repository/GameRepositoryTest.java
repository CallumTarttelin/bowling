package com.saskcow.bowling.repository;

import com.saskcow.bowling.BowlingApplication;
import com.saskcow.bowling.domain.Game;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {BowlingApplication.class})
@ActiveProfiles("test")
public class GameRepositoryTest {

    @Autowired
    private GameRepository repo;

    @Test
    public void thingsSaved_canBeRetrieved() {
        Game bestGame = new Game(null, null, "brian", null);
        repo.save(bestGame);
        Optional<Game> foundGame = repo.findById(bestGame.getId());
        assertThat(foundGame.get().getVenue()).isEqualTo(bestGame.getVenue());

    }


}