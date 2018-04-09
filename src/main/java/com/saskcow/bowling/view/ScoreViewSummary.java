package com.saskcow.bowling.view;

import com.saskcow.bowling.domain.Score;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ScoreViewSummary {
    private Long id;
    private Integer scratch;
    private Integer handicapped;
    private Integer score;

    public ScoreViewSummary(Score score) {
        this.id = score.getId();
        this.scratch = score.getScratch();
        this.handicapped = score.getHandicapped();
        this.score = score.getScore();
    }
}
