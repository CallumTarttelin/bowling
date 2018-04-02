package com.saskcow.bowling.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PlayerGame {
    private @GeneratedValue @Id Long id;
    @ManyToOne
    private Player player;
    @ManyToOne
    private Team team;
    @ManyToOne
    private Game game;
    @OneToMany(mappedBy = "playerGame", cascade = CascadeType.ALL)
    private List<Score> setScores;
}
