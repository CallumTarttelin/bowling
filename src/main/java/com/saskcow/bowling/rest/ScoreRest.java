package com.saskcow.bowling.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreRest {
    private Long playerGameId;
    private Integer scratch;
    private Integer handicap;

    public ScoreRest(Long playerGameId, Integer scratch){
        this.playerGameId = playerGameId;
        this.scratch = scratch;
    }
}
