package com.saskcow.bowling.repository;

import com.saskcow.bowling.domain.Game;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, Long>{
}
