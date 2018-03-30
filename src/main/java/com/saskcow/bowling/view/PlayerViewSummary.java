package com.saskcow.bowling.view;

import com.saskcow.bowling.domain.Player;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerViewSummary {
    private Long id;
    private String name;

    public PlayerViewSummary(Player player){
        this.id = player.getId();
        this.name = player.getName();
    }
}
