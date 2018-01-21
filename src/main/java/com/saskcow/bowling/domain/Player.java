package com.saskcow.bowling.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

import static org.aspectj.runtime.internal.Conversions.intValue;

@Data
@Entity
public class Player {
    private @Id @GeneratedValue Long id;
    private String name;
    @OneToMany
    private List<Score> scores;
    @ManyToOne
    private Team team;
    private List<Integer> pins;

    private Player() {}

    public Player(String name, List<Score> scores, Team team) {
        this.name = name;
        this.scores = scores;
        this.team = team;
    }

    public Integer getHandicap(){
        int rawHandicap = intValue((200 - intValue(scores.stream().mapToInt(Score::getScratch).average())) * 0.8);
        if(rawHandicap >= 0 && rawHandicap <= 80){
            return rawHandicap;
        } else if (rawHandicap < 0) {
            return 0;
        } else {
            return  80;
        }
    }
}
