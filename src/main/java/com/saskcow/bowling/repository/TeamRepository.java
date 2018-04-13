package com.saskcow.bowling.repository;

import com.saskcow.bowling.domain.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<Team, Long> {

    @Query("SELECT MAX(s.scratch) FROM Team t JOIN t.playerGames pg JOIN pg.scores s WHERE t.id = :id AND s.total = FALSE AND pg.player IS NULL")
    Integer highHandicapGame(Long id);

    @Query("SELECT MAX(s.scratch) FROM Team t JOIN t.playerGames pg JOIN pg.scores s WHERE t.id = :id AND s.total = TRUE AND pg.player IS NULL")
    Integer highHandicapSeries(Long id);

    @Query("SELECT SUM(s.scratch) FROM Team t JOIN t.playerGames pg JOIN pg.scores s WHERE t.id = :id AND s.total = TRUE AND pg.player IS NULL")
    Integer pinsFor(Long id);

    @Query("SELECT SUM(s.scratch) FROM Team t JOIN t.games g JOIN g.playerGames pg JOIN pg.scores s WHERE t.id = :id AND pg.team.id <> :id AND pg.player IS NULL AND s.total = TRUE")
    Integer pinsAgainst(Long id);

    @Query("SELECT SUM(s.score) FROM Team t JOIN t.playerGames pg JOIN pg.scores s WHERE t.id = :id AND pg.player IS NULL")
    Integer teamPoints(Long id);

    @Query("SELECT SUM(s.score) FROM Team t JOIN t.playerGames pg JOIN pg.scores s WHERE t.id = :id")
    Integer totalPoints(Long id);
}
