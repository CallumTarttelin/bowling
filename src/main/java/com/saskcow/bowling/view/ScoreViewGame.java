package com.saskcow.bowling.view;

import com.saskcow.bowling.domain.Score;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ScoreViewGame {
    private String player;
    private Integer score;
    private Integer scratch;

    public ScoreViewGame(Score score) {
        this.player = score.getPlayer().getName();
        this.score = score.getScore();
        this.scratch = score.getScratch();
    }
}
