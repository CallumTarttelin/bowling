package com.saskcow.bowling.view;

import com.saskcow.bowling.domain.Team;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TeamViewSummary {
    private Long id;
    private String name;
    private Integer pinsFor;
    private Integer pinsAgainst;
    private Integer highHandicapGame;
    private Integer highHandicapSeries;
    private Integer teamPoints;
    private Integer totalPoints;


    public TeamViewSummary(Team team) {
        this.name = team.getName();
        this.id = team.getId();
    }
}
