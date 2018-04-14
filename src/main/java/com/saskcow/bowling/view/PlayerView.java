package com.saskcow.bowling.view;

import com.saskcow.bowling.domain.Player;
import com.saskcow.bowling.domain.PlayerGame;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class PlayerView {
    private Long id;
    private TeamViewSummary team;
    private String name;
    private Integer handicap;
    private List<PlayerGameViewSummary> recentGames;
    private Integer highGame;
    private Integer lowGame;
    private Integer highSeries;
    private Integer lowSeries;

    public PlayerView(Player player){
        this.id = player.getId();
        this.name = player.getName();
        this.team = new TeamViewSummary(player.getTeam());
        this.handicap = player.getHandicap();
        if (player.getPlayerGames() != null) {
            List<PlayerGame> last6 = player.getPlayerGames().stream()
                    .filter(playerGame -> playerGame.getScores().size() == 4)
                    .collect(Collectors.toList());
            this.recentGames = last6.subList(0, last6.size() >= 6? 6 : last6.size()).stream().map(PlayerGameViewSummary::new).collect(Collectors.toList());
        }
    }
}
