package com.saskcow.bowling.view;

import com.saskcow.bowling.domain.League;
import com.saskcow.bowling.domain.Team;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class TeamView {
    private League league;
    private Long id;
    private String name;
    private List<PlayerViewSummary> players;


    public TeamView(Team team) {
        this.league = team.getLeague();
        this.name = team.getName();
        this.id = team.getId();
        this.players = team.getPlayers() == null ? null : team.getPlayers().stream().map(PlayerViewSummary::new).collect(Collectors.toList());
    }
}
