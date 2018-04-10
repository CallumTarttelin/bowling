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

}
