package com.saskcow.bowling.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Rota {
    @OneToOne
    private League league;
    private @Id @GeneratedValue Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Integer weeklyGames;

    public Rota (League league) {
        this.league = league;
    }

    public Rota (League league, LocalDateTime start, LocalDateTime end, Integer weeklyGames){
        this.league = league;
        this.start = start;
        this.end = end;
        this.weeklyGames = weeklyGames;
    }
}
