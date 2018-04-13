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

    public PlayerView(Player player){
        this.id = player.getId();
        this.name = player.getName();
        this.team = new TeamViewSummary(player.getTeam());
        this.handicap = player.getHandicap();
        if (player.getPlayerGames() != null) {
            List<PlayerGame> last24 = player.getPlayerGames().stream()
                    .filter(playerGame -> playerGame.getScores().size() == 3 || playerGame.getScores().size() == 4)
                    .collect(Collectors.toList());
            this.recentGames = last24.subList(0, last24.size() >= 6? 6 : last24.size()).stream().map(PlayerGameViewSummary::new).collect(Collectors.toList());
        }
    }
}
