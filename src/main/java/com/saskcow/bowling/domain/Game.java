package com.saskcow.bowling.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
@ToString(exclude="teams")
public class Game {
    private @Id @GeneratedValue Long id;
    @ManyToOne
    private Rota rota;
    private LocalDateTime time;
    private String venue;
    @ManyToMany
    private List<Team> teams;
    @OneToMany(cascade = {CascadeType.ALL})
    private List<PlayerGame> playerGames;

    public Game(Rota rota, LocalDateTime time, String venue, List<Team> teams) {
        this.rota = rota;
        this.time = time;
        this.venue = venue;
        this.teams = teams;
    }

    public void addPlayerGame(PlayerGame playerGame) {
        this.playerGames.add(playerGame);
    }

    public void addPlayerGame(Integer index, PlayerGame playerGame) {
        this.playerGames.add(index, playerGame);
    }

    public void addAllPlayerGame(List<PlayerGame> playerGames) {
        this.playerGames.addAll(playerGames);
    }

    public void deletePlayerGame(PlayerGame playerGame) {
        this.playerGames.remove(playerGame);
    }

    private void getScores() {
        // Add total Scores
        this.playerGames.forEach(playerGame -> playerGame.addScore(new Score(
                playerGame,
                playerGame.getScores().stream().reduce(0, (a, b) -> a + b.getScratch(), (a, b) -> a + b),
                playerGame.getScores().stream()
                        .reduce(0,
                                (a, b) -> a + b.getHandicapped(), (a, b) -> a + b) -
                        playerGame.getScores().stream()
                                .reduce(0, (a, b) -> a + b.getScratch(), (a, b) -> a + b)
                , true
        )));
    }

    private void addPoints() {
        // Zip List
        List<List<PlayerGame>> zipped = IntStream.range(0, 4)
                .mapToObj(i -> Arrays.asList(this.playerGames.get(i), this.playerGames.get(i + 4)))
                .collect(Collectors.toList());

        zipped.forEach(thing -> {
            // Zip Scores
            List<Score> scores0 = thing.get(0).getScores();
            List<Score> scores1 = thing.get(1).getScores();
            List<List<Score>> zippedScores = IntStream.range(0, 4)
                    .mapToObj(i -> Arrays.asList(scores0.get(i), scores1.get(i)))
                    .collect(Collectors.toList());
            // Compare each score
            zippedScores.forEach(scores -> {
                if (scores.get(0).getHandicapped().equals(scores.get(1).getHandicapped())) {
                    scores.get(0).setScore(1); scores.get(1).setScore(1);
                } else if (scores.get(0).getHandicapped() > scores.get(1).getHandicapped()){
                    scores.get(0).setScore(2); scores.get(1).setScore(0);
                } else {
                    scores.get(0).setScore(0); scores.get(1).setScore(2);
                }
            });
        });
    }

    private void addTotalPlayer() {
        List<PlayerGame> list1 = this.playerGames.subList(0, 3);
        List<PlayerGame> list2 = this.playerGames.subList(4, 7);
        this.playerGames.get(3).setScores(new LinkedList<>(
                IntStream.range(0, 3)
                        .mapToObj(i -> {
                            Integer x = list1.stream().reduce(0,
                                    (a, b) -> a + b.getScores().get(i).getScratch(),
                                    (a, b) -> a + b);
                            return (new Score(playerGames.get(3),
                                    x,
                                    list1.stream().reduce(0,
                                            (a, b) -> a + b.getScores().get(i).getHandicapped(),
                                            (a, b) -> a + b) - x)
                            );
                        }).collect(Collectors.toList())
        ));
        this.playerGames.get(7).setScores(new LinkedList<>(
                IntStream.range(0, 3)
                        .mapToObj(i -> {
                            Integer x = list2.stream().reduce(0,
                                    (a, b) -> a + b.getScores().get(i).getScratch(),
                                    (a, b) -> a + b);
                            return (new Score(playerGames.get(7),
                                    x,
                                    list2.stream().reduce(0,
                                            (a, b) -> a + b.getScores().get(i).getHandicapped(),
                                            (a, b) -> a + b) - x)
                            );
                        }).collect(Collectors.toList())
        ));
        this.teams.get(0).addPlayerGame(this.playerGames.get(3));
        this.teams.get(1).addPlayerGame(this.playerGames.get(7));
    }

    public void completeGame() {
        this.addTotalPlayer();
        this.getScores();
        this.addPoints();
    }

}
