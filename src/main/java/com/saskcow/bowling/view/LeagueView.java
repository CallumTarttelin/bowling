package com.saskcow.bowling.view;


import com.saskcow.bowling.domain.League;
import com.saskcow.bowling.domain.Team;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class LeagueView {
    private Long id;
    private String name;
    private List<String> teams;


    public LeagueView(League league) {
        this.name = league.getName();
        this.id = league.getId();
        this.teams = league.getTeams().stream().map(Team::getName).collect(Collectors.toList());
    }
}
