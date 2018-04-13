package com.saskcow.bowling.view;

import com.saskcow.bowling.domain.PlayerGame;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class PlayerGameViewSummary {
    private LocalDateTime date;
    private TeamViewSummary opposition;
    private List<ScoreViewSummary> scores;

    public PlayerGameViewSummary(PlayerGame playerGame) {
        this.date = playerGame.getGame().getTime();
        this.opposition = new TeamViewSummary(playerGame.getGame().getTeams().get((playerGame.getGame().getTeams().indexOf(playerGame.getTeam()) + 1) % 2));
        this.scores = playerGame.getScores().stream().map(ScoreViewSummary::new).collect(Collectors.toList());
    }
}

