package com.saskcow.bowling.view;

import com.saskcow.bowling.domain.Game;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class GameViewSummary {
    private Long id;
    private Date date;
    private String venue;

    public GameViewSummary(Game game){
        this.id = game.getId();
        this.date = game.getTime();
        this.venue = game.getVenue();
    }
}
