package com.saskcow.bowling.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@Entity
public class Score {
    private @Id @GeneratedValue Long id;
    @ManyToOne
    private Game game;
    @ManyToOne
    private Player player;

    private Score(){}

    public Score(Game game, Player player) {
        this.game = game;
        this.player = player;
    }
}
