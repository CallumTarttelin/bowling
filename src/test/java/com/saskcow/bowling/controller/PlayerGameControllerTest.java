package com.saskcow.bowling.controller;

import com.saskcow.bowling.domain.*;
import com.saskcow.bowling.repository.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class PlayerGameControllerTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("reports/java-snippets");

    @Mock
    private PlayerGameRepository repo;
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private GameRepository gameRepository;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new PlayerGameController(repo, playerRepository, teamRepository, gameRepository))
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Test
    public void addPlayerGame_shouldSavePlayerGame() throws Exception {
        League cityWatch = new League(1L, "City Watch", null, new LinkedList<>());
        cityWatch.setRota(new Rota(cityWatch));

        Team cableStreet = new Team(1L, "Cable Street Particulars", null, null, cityWatch, new LinkedList<>());
        Team nightWatch = new Team(2L, "The Night Watch", null, null, cityWatch, new LinkedList<>());

        cableStreet.setPlayers(new LinkedList<>(Arrays.asList(
                new Player(1L, "Findthee Swing", cableStreet, new LinkedList<>()),
                new Player(2L, "Carcer", cableStreet, new LinkedList<>()),
                new Player(3L, "Gerald Leastways, a.k.a. Ferret", cableStreet, new LinkedList<>()),
                new Player(4L, "Henry 'The Hamster' Higgins", cableStreet, new LinkedList<>())
        )));
        nightWatch.setPlayers(new LinkedList<>(Arrays.asList(
                new Player(5L, "Sam Vimes", nightWatch, new LinkedList<>()),
                new Player(6L, "Carrot Ironfoundersson", nightWatch, new LinkedList<>()),
                new Player(7L, "Nobby Nobbs", nightWatch, new LinkedList<>()),
                new Player(8L, "Fred Colon", nightWatch, new LinkedList<>())
        )));

        Game game = new Game(1L, cityWatch.getRota(), LocalDateTime.now(Clock.systemUTC()), "The Shades", Arrays.asList(cableStreet, nightWatch), new LinkedList<>());
        cableStreet.addGame(game); nightWatch.addGame(game);

        PlayerGame swingGame = new PlayerGame(1L, cableStreet.getPlayers().get(0), cableStreet, game, new LinkedList<>());
        PlayerGame carcerGame = new PlayerGame(2L, cableStreet.getPlayers().get(1), cableStreet, game, new LinkedList<>());
        PlayerGame geraldGame = new PlayerGame(3L, cableStreet.getPlayers().get(2), cableStreet, game, new LinkedList<>());

        when(repo.save(new PlayerGame(cableStreet.getPlayers().get(0), cableStreet, game))).thenReturn(swingGame);
        when(repo.save(new PlayerGame(cableStreet.getPlayers().get(1), cableStreet, game))).thenReturn(carcerGame);
        when(repo.save(new PlayerGame(cableStreet.getPlayers().get(2), cableStreet, game))).thenReturn(geraldGame);

        when(GameRepository.save(isA(Game.class))).thenReturn(game);
        when(GameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        when(GameRepository.save(isA(Game.class))).thenReturn(game);
        when(repo.findById(game.getId())).thenReturn(Optional.of(swingGame));
        when(repo.findById(game.getId())).thenReturn(Optional.of(carcerGame));

        mockMvc.perform(post("/api/score")
                .content("{\"playerGameId\": \"1\", \"scratch\": \"103\", \"handicap\": \"3\"}")
                .contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost:8080/api/score/" + score3.getId()))
                .andDo(document("player/create"))
                .andReturn().getResponse().getHeader("Location");

        assertThat(game.getPlayerGames().get(0)).isEqualTo(swingGame);
        assertThat(game.getPlayerGames().get(1)).isEqualTo(carcerGame);
        assertThat(game.getPlayerGames().get(2)).isEqualTo(geraldGame);
        assertThat(game.getPlayerGames().size()).isEqualTo(3);
        verify(repo, times(1)).save(new PlayerGame(cableStreet.getPlayers().get(0), cableStreet, game));
        verify(repo, times(1)).save(new PlayerGame(cableStreet.getPlayers().get(1), cableStreet, game));
        verify(repo, times(1)).save(new PlayerGame(cableStreet.getPlayers().get(2), cableStreet, game));
        verify(playerRepository, times(3)).save(Player.class);
        verify(repo, times(1)).save(new PlayerGame(cableStreet.getPlayers().get(1), cableStreet, game));
        verify(repo, times(1)).save(new PlayerGame(cableStreet.getPlayers().get(2), cableStreet, game));
    }

    @Test
    public void deleteScore_shouldDeleteScore() throws Exception {
        PlayerGame playerGame = new PlayerGame(1L, null, null, null, new LinkedList<>());
        Score score1 = new Score(1L, playerGame, 101, 104, null);
        Score score2 = new Score(2L, playerGame, 102, 105, null);
        Score score3 = new Score(3L, playerGame, 103, 106, null);
        playerGame.addScore(score1); playerGame.addScore(score2); playerGame.addScore(score3);

        doNothing().when(repo).deleteById(isA(Long.class));
        when(repo.findById(score3.getId())).thenReturn(Optional.of(score3));

        mockMvc.perform(delete("/api/score/3"))
                .andExpect(status().isNoContent())
                .andDo(document("player/delete"));
        verify(repo, times(1)).deleteById(3L);
        assertThat(playerGame.getScores()).doesNotContain(score3);
        assertThat(playerGame.getScores()).contains(score1);
    }

}