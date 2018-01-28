package com.saskcow.bowling.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Game {
    private @Id @GeneratedValue Long id;
    private Date time;
    private String venue;
    @OneToMany
    private List<Team> teams;
    @ManyToOne
    private League league;

    public Game(Date time, String venue, List<Team> teams, League league) {
        this.time = time;
        this.venue = venue;
        this.teams = teams;
        this.league = league;
    }
}
