package com.saskcow.bowling.view;


import com.saskcow.bowling.domain.League;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class LeagueView {
    private Long id;
    private String name;
    private Long rotaId;
    private List<TeamViewLeague> teams;
    private List<GameViewSummary> games;


    public LeagueView(League league) {
        this.name = league.getName();
        this.id = league.getId();
        this.rotaId = league.getRota().getId();
        this.teams = league.getTeams() == null ? new LinkedList<>() : league.getTeams().stream().map(TeamViewLeague::new).collect(Collectors.toList());
        this.games = league.getGames() == null ? new LinkedList<>() : league.getGames().stream().map(GameViewSummary::new).collect(Collectors.toList());
    }
}
