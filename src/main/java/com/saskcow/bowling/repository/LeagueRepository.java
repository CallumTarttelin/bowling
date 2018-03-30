package com.saskcow.bowling.repository;

import com.saskcow.bowling.domain.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LeagueRepository extends CrudRepository<League, Long> {
    List<League> findByNameContaining(String name);
}
