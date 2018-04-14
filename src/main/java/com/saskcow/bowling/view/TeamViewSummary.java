package com.saskcow.bowling.view;

import com.saskcow.bowling.domain.Team;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TeamViewSummary {
    private Long id;
    private String name;

    public TeamViewSummary(Team team) {
        this.name = team.getName();
        this.id = team.getId();
    }
}
