package com.saskcow.bowling.view;

import com.saskcow.bowling.domain.Team;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TeamScoreView {
    private String name;
    private Integer totalPoints;

    public TeamScoreView(Team team){
        this.name = team.getName();
        this.totalPoints = team.getPlayers().stream().map(player -> player.getScore());
    }
}
