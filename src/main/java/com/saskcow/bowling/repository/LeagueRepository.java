package com.saskcow.bowling.repository;

import com.saskcow.bowling.domain.League;
import org.springframework.data.repository.CrudRepository;

public interface LeagueRepository extends CrudRepository<League, Long> {
}
