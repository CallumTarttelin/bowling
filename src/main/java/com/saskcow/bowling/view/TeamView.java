package com.saskcow.bowling.view;

import com.saskcow.bowling.domain.League;
import com.saskcow.bowling.domain.Team;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class TeamView {
    private League league;
    private Long id;
    private String name;
    private List<String> players;


    public TeamView(Team team) {
        this.league = team.getLeague();
        this.name = team.getName();
        this.id = team.getId();
        this.players = new ArrayList<>();
        team.getPlayers().forEach(player -> this.players.add(player.getName()));
    }
}
