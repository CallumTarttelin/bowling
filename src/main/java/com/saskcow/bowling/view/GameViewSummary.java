package com.saskcow.bowling.view;

import com.saskcow.bowling.domain.Game;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class GameViewSummary {
    private Long id;
    private String time;
    private String venue;

    public GameViewSummary(Game game){
        this.id = game.getId();
        this.time = game.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.venue = game.getVenue();
    }
}
