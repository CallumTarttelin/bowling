package com.saskcow.bowling.view;

import com.saskcow.bowling.domain.PlayerGame;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class PlayerGameView {
    private Long id;
//    private GameViewSummary game;
    private PlayerViewSummary player;
    private TeamViewSummary team;
    private List<ScoreViewSummary> scores;

    public PlayerGameView (PlayerGame playerGame) {
        this.id = playerGame.getId();
//        this.game = new GameViewSummary(playerGame.getGame());
        this.player = playerGame.getPlayer() != null ? new PlayerViewSummary(playerGame.getPlayer()) : null;
        this.team = new TeamViewSummary(playerGame.getTeam());
        this.scores = playerGame.getScores().stream().map(ScoreViewSummary::new).collect(Collectors.toList());
    }
}
