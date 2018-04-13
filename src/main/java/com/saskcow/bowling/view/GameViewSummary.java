package com.saskcow.bowling.view;

import com.saskcow.bowling.domain.Game;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class GameViewSummary {
    private Long id;
    private String time;
    private List<TeamViewSummary> teams;
    private String venue;
    private LeagueViewSummary league;
    private Integer winner;

    public GameViewSummary(Game game){
        this.id = game.getId();
        this.time = game.getTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.teams = game.getTeams().stream().map(TeamViewSummary::new).collect(Collectors.toList());
        this.venue = game.getVenue();
        this.league = new LeagueViewSummary(game.getTeams().get(0).getLeague());
        if (game.getPlayerGames().size() == 8) this.winner = game.getPlayerGames().subList(0, 4).stream().reduce(0,
                (a, b) -> a + b.getScores().stream().reduce(0,
                        (c, d) -> c + d.getScore(),
                        (c, d) -> c + d),
                (a, b) -> a + b) >
                game.getPlayerGames().subList(4, 8).stream().reduce(0,
                        (a, b) -> a + b.getScores().stream().reduce(0,
                                (c, d) -> c + d.getScore(),
                                (c, d) -> c + d),
                        (a, b) -> a + b) ? 0 : 1;
    }
}
