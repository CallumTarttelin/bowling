package com.saskcow.bowling.view;

import com.saskcow.bowling.domain.Game;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class GameView {
    private Long id;
    private Date date;
    private List<TeamViewSummary> teams;
    private String venue;
    private LeagueViewSummary league;

    public GameView(Game game){
        this.id = game.getId();
        this.date = game.getTime();
        this.teams = game.getTeams().stream().map(TeamViewSummary::new).collect(Collectors.toList());
        this.venue = game.getVenue();
        this.league = new LeagueViewSummary(game.getLeague());
    }
}
