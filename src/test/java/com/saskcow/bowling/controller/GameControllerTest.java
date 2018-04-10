package com.saskcow.bowling.controller;

import com.saskcow.bowling.domain.*;
import com.saskcow.bowling.repository.GameRepository;
import com.saskcow.bowling.repository.RotaRepository;
import com.saskcow.bowling.repository.TeamRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.IntStream;

import static java.lang.Math.pow;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class GameControllerTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("reports/java-snippets");

    @Mock
    private GameRepository repo;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private RotaRepository rotaRepository;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new GameController(repo, teamRepository, rotaRepository))
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Test
    public void addGame_shouldSaveTheGame() throws Exception {
        League league = new League(1L, "Brian", null, new LinkedList<>());
        Rota rota = new Rota(1L, league, new LinkedList<>(), null, null, null);
        Team team1 = new Team(1L, "Dave", null, new LinkedList<>(), league, new LinkedList<>());
        Team team2 = new Team(2L, "David", null, new LinkedList<>(), league, new LinkedList<>());
        LocalDateTime dateTime = LocalDateTime.now(Clock.systemUTC());
        Game game = new Game(1L, rota, dateTime, "Brian Bowling Centre", new LinkedList<>(Arrays.asList(team1, team2)), new LinkedList<>());
        team1.addGame(game);
        team2.addGame(game);
        rota.addGame(game);
        league.addTeam(team1);
        league.addTeam(team2);
        league.setRota(rota);
        when(repo.save(isA(Game.class))).thenReturn(game);
        when(repo.findById(game.getId())).thenReturn(Optional.of(game));
        when(teamRepository.findById(team1.getId())).thenReturn(Optional.of(team1));
        when(teamRepository.findById(team2.getId())).thenReturn(Optional.of(team2));
        when(rotaRepository.findById(rota.getId())).thenReturn(Optional.of(rota));


        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String content = String.format("{\"time\":\"%s\", \"venue\": \"Brian Bowling Centre\", \"rotaId\": \"1\", \"teamId1\": \"1\", \"teamId2\": \"2\"}", dateTime.format(formatter));
        String uri = mockMvc.perform(post("/api/game")
                .content(content)
                .contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost:8080/api/game/" + game.getId()))
                .andDo(document("game/create"))
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.venue", equalTo("Brian Bowling Centre")))
                .andExpect(MockMvcResultMatchers.jsonPath("venue", equalTo("Brian Bowling Centre")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.league.name", equalTo("Brian")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.time", equalTo(dateTime.format(formatter))))
                .andDo(document("game/get"));
    }

    @Test
    public void deleteGame_shouldDeleteGame() throws Exception {
        League league = new League(1L, "Brian", null, new LinkedList<>());
        Rota rota = new Rota(1L, league, new LinkedList<>(), null, null, null);
        Team team1 = new Team(1L, "Brian", null, new LinkedList<>(), league, new LinkedList<>());
        Team team2 = new Team(2L, "Brian", null, new LinkedList<>(), league, new LinkedList<>());
        LocalDateTime dateTime = LocalDateTime.now(Clock.systemUTC());
        Game game = new Game(1L, rota, dateTime, "Brian Bowling Centre", new LinkedList<>(Arrays.asList(team1, team2)), new LinkedList<>());
        Game game2 = new Game(2L, rota, dateTime, "Brian Bowling Centre", new LinkedList<>(Arrays.asList(team1, team2)), new LinkedList<>());
        team1.addGame(game);
        team2.addGame(game);
        rota.addGame(game);
        team1.addGame(game2);
        team2.addGame(game2);
        rota.addGame(game2);
        league.addTeam(team1);
        league.addTeam(team2);
        league.setRota(rota);

        doNothing().when(repo).deleteById(isA(Long.class));
        when(repo.findById(game.getId())).thenReturn(Optional.of(game));

        mockMvc.perform(delete("/api/game/1"))
                .andExpect(status().isNoContent())
                .andDo(document("game/delete"));
        verify(repo, times(1)).deleteById(1L);
        assertThat(team1.getGames()).doesNotContain(game);
        assertThat(team1.getGames()).contains(game2);
        assertThat(league.getGames()).doesNotContain(game);
        assertThat(league.getGames()).contains(game2);
    }

    @Test
    public void completeGame_shouldCompleteGame() throws Exception {
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
        cableStreet.addAllPlayerGame(Arrays.asList(swingGame, carcerGame, geraldGame));
        game.addAllPlayerGame(Arrays.asList(swingGame, carcerGame, geraldGame));
        cableStreet.getPlayers().get(0).addPlayerGame(swingGame); cableStreet.getPlayers().get(1).addPlayerGame(carcerGame); cableStreet.getPlayers().get(2).addPlayerGame(geraldGame);

        PlayerGame samGame = new PlayerGame(4L, nightWatch.getPlayers().get(0), nightWatch, game, new LinkedList<>());
        PlayerGame carrotGame = new PlayerGame(5L, nightWatch.getPlayers().get(1), nightWatch, game, new LinkedList<>());
        PlayerGame nobbyGame = new PlayerGame(6L, nightWatch.getPlayers().get(2), nightWatch, game, new LinkedList<>());
        nightWatch.addAllPlayerGame(Arrays.asList(samGame, carrotGame, nobbyGame));
        game.addAllPlayerGame(Arrays.asList(samGame, carrotGame, nobbyGame));
        nightWatch.getPlayers().get(0).addPlayerGame(samGame); nightWatch.getPlayers().get(1).addPlayerGame(carrotGame); nightWatch.getPlayers().get(2).addPlayerGame(nobbyGame);

        when(repo.findById(game.getId())).thenReturn(Optional.of(game));

        mockMvc.perform(post("/api/game/1"))
                .andExpect(status().isBadRequest());

        IntStream.range(0, 3).forEach(i -> swingGame.addScore(new Score((long) i, swingGame, 100 + 10*i, 100 + 10*i + 15, null)));
        IntStream.range(0, 3).forEach(i -> carcerGame.addScore(new Score((long) i + 3, carcerGame, 100 - 10*i, 100 - 10*i + 25, null)));
        IntStream.range(0, 3).forEach(i -> geraldGame.addScore(new Score((long) i + 6, geraldGame, 100 + 5*i, 100 + 5*i + 20, null)));
        IntStream.range(0, 3).forEach(i -> samGame.addScore(new Score((long) i + 9, samGame, 100 + (4*i*i), 100 + (4*i*i) + 15, null)));
        IntStream.range(0, 3).forEach(i -> carrotGame.addScore(new Score((long) i + 12, carrotGame, 100, 100 + 25, null)));
        IntStream.range(0, 3).forEach(i -> nobbyGame.addScore(new Score((long) i + 15, nobbyGame, (int) (100 + pow(-5*i, i)), (int) (100 + pow(-5*i, i)) + 20, null)));

        mockMvc.perform(post("/api/game/1"))
                .andExpect(status().isNoContent())
                .andDo(document("game/complete"));

        assertThat(game.getPlayerGames().stream().allMatch(playerGame -> playerGame.getScores().stream().allMatch(score -> score.getScore() != null))).isTrue();
        assertThat(game.getPlayerGames().stream().allMatch(playerGame -> playerGame.getScores().size() == 4)).isTrue();
        assertThat(game.getPlayerGames().size()).isEqualTo(8);

    }

}