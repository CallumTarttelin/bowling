package com.saskcow.bowling.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Game {
    //TODO add Pins for Against GGH stuff HHS stuff and points
    private @Id @GeneratedValue Long id;
    private Date time;
    private String venue;
    @OneToMany
    private List<Team> teams;
    @OneToMany
    private List<Score> scores;
    @ManyToOne
    private League league;

    private Game() {}

    public Game(Date time, String venue, List<Team> teams, List<Score> scores, League league) {
        this.time = time;
        this.venue = venue;
        this.teams = teams;
        this.scores = scores;
        this.league = league;
    }
}
