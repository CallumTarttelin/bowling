package com.saskcow.bowling.repository;

import com.saskcow.bowling.domain.Player;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<Player, Long> {

    @Query("SELECT MAX(s.scratch) FROM Player p JOIN p.playerGames pg JOIN pg.scores s WHERE p.id = :id AND s.total = FALSE")
    Integer highGame(Long id);

    @Query("SELECT MAX(s.scratch) FROM Player p JOIN p.playerGames pg JOIN pg.scores s WHERE p.id = :id AND s.total = TRUE ")
    Integer highSeries(Long id);

    @Query("SELECT MIN(s.scratch) FROM Player p JOIN p.playerGames pg JOIN pg.scores s WHERE p.id = :id AND s.total = FALSE")
    Integer lowGame(Long id);

    @Query("SELECT MIN(s.scratch) FROM Player p JOIN p.playerGames pg JOIN pg.scores s WHERE p.id = :id AND s.total = TRUE ")
    Integer lowSeries(Long id);
}
