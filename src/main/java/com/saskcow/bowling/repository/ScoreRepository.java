package com.saskcow.bowling.repository;

import com.saskcow.bowling.domain.Score;
import org.springframework.data.repository.CrudRepository;

public interface ScoreRepository extends CrudRepository<Score, Long> {
}
