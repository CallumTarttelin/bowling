package com.saskcow.bowling.controller;

import com.saskcow.bowling.domain.Game;
import com.saskcow.bowling.domain.League;
import com.saskcow.bowling.domain.Rota;
import com.saskcow.bowling.domain.Team;
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
import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;

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
        Team team1 = new Team(1L, "Dave", null, new LinkedList<>(), league);
        Team team2 = new Team(2L, "David", null, new LinkedList<>(), league);
        LocalDateTime dateTime = LocalDateTime.now(Clock.systemUTC());
        Game game = new Game(1L, rota, dateTime, "Brian Bowling Centre", new LinkedList<>(Arrays.asList(team1, team2)));
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
        Team team1 = new Team(1L, "Brian", null, new LinkedList<>(), league);
        Team team2 = new Team(2L, "Brian", null, new LinkedList<>(), league);
        LocalDateTime dateTime = LocalDateTime.now(Clock.systemUTC());
        Game game = new Game(1L, rota, dateTime, "Brian Bowling Centre", new LinkedList<>(Arrays.asList(team1, team2)));
        Game game2 = new Game(2L, rota, dateTime, "Brian Bowling Centre", new LinkedList<>(Arrays.asList(team1, team2)));
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

}