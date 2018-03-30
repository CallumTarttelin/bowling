package com.saskcow.bowling.view;

import com.saskcow.bowling.domain.Team;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class TeamView {
    private LeagueViewSummary league;
    private Long id;
    private String name;
    private List<PlayerViewSummary> players;
    private List<GameView> games;


    public TeamView(Team team) {
        this.league = new LeagueViewSummary(team.getLeague());
        this.name = team.getName();
        this.id = team.getId();
        this.players = team.getPlayers() == null ? null : team.getPlayers().stream().map(PlayerViewSummary::new).collect(Collectors.toList());
        this.games = team.getGames()== null ? null : team.getGames().stream().map(GameView::new).collect(Collectors.toList());
    }
}
