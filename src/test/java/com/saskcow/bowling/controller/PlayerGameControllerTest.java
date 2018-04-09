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

        Team cableStreet = new Team(1L, "Cable Street Particulars", null, new LinkedList<>(), cityWatch, new LinkedList<>());
        Team nightWatch = new Team(2L, "The Night Watch", null, new LinkedList<>(), cityWatch, new LinkedList<>());

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

        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        when(playerRepository.findById(1L)).thenReturn(Optional.of(swingGame.getPlayer()));
        when(playerRepository.findById(2L)).thenReturn(Optional.of(carcerGame.getPlayer()));
        when(playerRepository.findById(3L)).thenReturn(Optional.of(geraldGame.getPlayer()));

        when(teamRepository.findById(cableStreet.getId())).thenReturn(Optional.of(cableStreet));

        mockMvc.perform(post("/api/playergame")
                .content("{\"playerIds\": [1, 2, 3], \"teamId\": \"1\", \"gameId\": \"1\"}")
                .contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost:8080/api/playergame/" + swingGame.getId()))
                .andDo(document("playergame/create"));

        assertThat(game.getPlayerGames().get(0)).isEqualTo(swingGame);
        assertThat(game.getPlayerGames().get(1)).isEqualTo(carcerGame);
        assertThat(game.getPlayerGames().get(2)).isEqualTo(geraldGame);
        assertThat(game.getPlayerGames().size()).isEqualTo(3);
        verify(repo, times(1)).save(new PlayerGame(cableStreet.getPlayers().get(0), cableStreet, game));
        verify(repo, times(1)).save(new PlayerGame(cableStreet.getPlayers().get(1), cableStreet, game));
        verify(repo, times(1)).save(new PlayerGame(cableStreet.getPlayers().get(2), cableStreet, game));
        verify(playerRepository, times(3)).save(isA(Player.class));
        verify(gameRepository, times(1)).save(isA(Game.class));
        verify(teamRepository, times(1)).save(isA(Team.class));
    }

    @Test
    public void deletePlayerGame_shouldDeletePlayerGame() throws Exception {
        Team cableStreet = new Team(1L, "Cable Street Particulars", null, new LinkedList<>(), null, new LinkedList<>());
        Team nightWatch = new Team(2L, "The Night Watch", null, new LinkedList<>(), null, new LinkedList<>());

        cableStreet.setPlayers(new LinkedList<>(Arrays.asList(
                new Player(1L, "Findthee Swing", cableStreet, new LinkedList<>()),
                new Player(2L, "Carcer", cableStreet, new LinkedList<>()),
                new Player(3L, "Gerald Leastways, a.k.a. Ferret", cableStreet, new LinkedList<>()),
                new Player(4L, "Henry 'The Hamster' Higgins", cableStreet, new LinkedList<>())
        )));

        Game game = new Game(1L, null, LocalDateTime.now(Clock.systemUTC()), "The Shades", Arrays.asList(cableStreet, nightWatch), new LinkedList<>());
        cableStreet.addGame(game); nightWatch.addGame(game);

        PlayerGame swingGame = new PlayerGame(1L, cableStreet.getPlayers().get(0), cableStreet, game, new LinkedList<>());
        PlayerGame carcerGame = new PlayerGame(2L, cableStreet.getPlayers().get(1), cableStreet, game, new LinkedList<>());
        PlayerGame geraldGame = new PlayerGame(3L, cableStreet.getPlayers().get(2), cableStreet, game, new LinkedList<>());

        cableStreet.getPlayers().get(0).addPlayerGame(swingGame); cableStreet.getPlayers().get(1).addPlayerGame(carcerGame); cableStreet.getPlayers().get(2).addPlayerGame(geraldGame);
        cableStreet.addPlayerGame(swingGame); cableStreet.addPlayerGame(carcerGame); cableStreet.addPlayerGame(geraldGame);
        game.addPlayerGame(swingGame); game.addPlayerGame(carcerGame); game.addPlayerGame(geraldGame);

        doNothing().when(repo).deleteById(isA(Long.class));

        when(repo.findById(swingGame.getId())).thenReturn(Optional.of(swingGame));

        mockMvc.perform(delete("/api/playergame/1"))
                .andExpect(status().isNoContent())
                .andDo(document("playergame/delete"));

        verify(repo, times(1)).deleteById(1L);
        assertThat(cableStreet.getPlayerGames()).doesNotContain(swingGame);
        assertThat(cableStreet.getPlayerGames()).contains(carcerGame);
        assertThat(cableStreet.getPlayerGames()).contains(geraldGame);
    }

}