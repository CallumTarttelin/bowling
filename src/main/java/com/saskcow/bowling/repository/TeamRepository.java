package com.saskcow.bowling.repository;

import com.saskcow.bowling.domain.Team;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<Team, Long> {
}
