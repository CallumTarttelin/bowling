package com.saskcow.bowling.repository;

import com.saskcow.bowling.BowlingApplication;
import com.saskcow.bowling.domain.League;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {BowlingApplication.class})
@ActiveProfiles("test")
public class LeagueRepositoryTest {

    @Autowired
    private LeagueRepository repo;



    @Test
    public void thingsSaved_canBeRetrieved() {
        League bestLeague = new League( "brian", null, null);
        repo.save(bestLeague);
        League foundGame = repo.findOne(bestLeague.getId());
        assertThat(foundGame.getName()).isEqualTo(bestLeague.getName());

    }

    @Test
    public void thingsSaved_canBeQueried() {
        League bestGame = new League( "Brian", null, null);
        League worstGame = new League( "Dave", null, null);
        repo.save(bestGame);
        repo.save(worstGame);
        List<League> foundGame = repo.findByNameContaining("Brian");
        assertThat(foundGame.size()).isOne();
        assertThat(foundGame.get(0).getName()).isEqualTo(bestGame.getName());
        foundGame = repo.findByNameContaining("Bri");
        assertThat(foundGame.size()).isOne();
        assertThat(foundGame.get(0).getName()).isEqualTo(bestGame.getName());
    }

    @Test
    public void thingsSaved_canBeDeleted() {
        League bestLeague = new League("brian", null, null);
        repo.save(bestLeague);
        repo.delete(bestLeague.getId());
        League foundGame = repo.findOne(bestLeague.getId());
        assertThat(foundGame).isEqualTo(null);
    }


}