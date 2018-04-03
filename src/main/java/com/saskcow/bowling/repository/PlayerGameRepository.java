package com.saskcow.bowling.repository;

import com.saskcow.bowling.domain.PlayerGame;
import org.springframework.data.repository.CrudRepository;

public interface PlayerGameRepository extends CrudRepository<PlayerGame, Long> {
}
