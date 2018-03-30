package com.saskcow.bowling.view;

import com.saskcow.bowling.domain.Player;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerView {
    private Long id;
    private TeamViewSummary team;
    private String name;

    public PlayerView(Player player){
        this.id = player.getId();
        this.name = player.getName();
        this.team = new TeamViewSummary(player.getTeam());
    }
}
