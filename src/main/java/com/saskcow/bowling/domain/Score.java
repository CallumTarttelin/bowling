package com.saskcow.bowling.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "playerGame")
public class Score {
    private @GeneratedValue @Id Long id;
    @ManyToOne
    private PlayerGame playerGame;
    private Integer scratch;
    private Integer handicapped;
    private Integer score;
    private boolean total;

    public Score(PlayerGame playerGame, Integer scratch){
        this.playerGame = playerGame;
        this.scratch = scratch;
        this.handicapped = scratch;
    }

    public Score(PlayerGame playerGame, Integer scratch, Integer handicap){
        this.playerGame = playerGame;
        this.scratch = scratch;
        this.handicapped = scratch + handicap;
    }

    public Score(PlayerGame playerGame, Integer scratch, Integer handicap, boolean total){
        this.playerGame = playerGame;
        this.scratch = scratch;
        this.total = total;
        this.handicapped = scratch + handicap;
    }

    public Score(Long id, PlayerGame playerGame, Integer scratch, Integer handicapped, Integer score){
        this.id = id;
        this.playerGame = playerGame;
        this.scratch = scratch;
        this.handicapped = handicapped;
        this.score = score;
    }

}
