package com.saskcow.bowling.repository;

import com.saskcow.bowling.domain.Player;
import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<Player, Long> {
}
