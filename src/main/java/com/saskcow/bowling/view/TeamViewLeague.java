package com.saskcow.bowling.view;

import com.saskcow.bowling.domain.Team;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TeamViewLeague {
    private Long id;
    private String name;
    private Integer pinsFor;
    private Integer pinsAgainst;
    private Integer highHandicapGame;
    private Integer highHandicapSeries;
    private Integer teamPoints;
    private Integer totalPoints;
    private Integer numGames;


    public TeamViewLeague(Team team) {
        this.name = team.getName();
        this.id = team.getId();
        this.numGames = team.getGames() != null ? (int) team.getGames().stream().filter(a -> a.getPlayerGames().size() == 8).count() : 0;
    }
}
