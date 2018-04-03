package com.saskcow.bowling.controller;

import com.saskcow.bowling.domain.PlayerGame;
import com.saskcow.bowling.domain.Score;
import com.saskcow.bowling.repository.PlayerGameRepository;
import com.saskcow.bowling.repository.ScoreRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
public class ScoreControllerTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("reports/java-snippets");

    @Mock
    private ScoreRepository repo;
    @Mock
    private PlayerGameRepository playerGameRepository;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ScoreController(repo, playerGameRepository))
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Test
    public void addScore_shouldSaveTheScore() throws Exception {
        PlayerGame playerGame = new PlayerGame(1L, null, null, null, new LinkedList<>());
        Score score1 = new Score(1L, playerGame, 101, 104, null);
        Score score2 = new Score(2L, playerGame, 102, 105, null);
        Score score3 = new Score(3L, playerGame, 103, 106, null);
        playerGame.addScore(score1); playerGame.addScore(score2);

        when(repo.save(isA(Score.class))).thenReturn(score3);
        when(playerGameRepository.save(isA(PlayerGame.class))).thenReturn(playerGame);
        when(playerGameRepository.findById(1L)).thenReturn(Optional.of(playerGame));

        mockMvc.perform(post("/api/score")
                .content("{\"playerGameId\": \"1\", \"scratch\": \"103\", \"handicap\": \"3\"}")
                .contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost:8080/api/score/" + score3.getId()))
                .andDo(document("player/create"))
                .andReturn().getResponse().getHeader("Location");

        assertThat(playerGame.getScores()).contains(score3);
        verify(repo, times(1)).save(isA(Score.class));
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