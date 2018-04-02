package com.saskcow.bowling.controller;

import com.saskcow.bowling.domain.Player;
import com.saskcow.bowling.domain.Team;
import com.saskcow.bowling.repository.PlayerRepository;
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
public class PlayerControllerTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("reports/java-snippets");

    @Mock
    private PlayerRepository repo;
    @Mock
    private TeamRepository teamRepository;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new PlayerController(repo, teamRepository))
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Test
    public void addPlayer_shouldSaveThePlayer() throws Exception {
        Team team = new Team(1L, "Brian's Bowlers!", new LinkedList<>(),null, null, new LinkedList<>());
        Player player = new Player(1L, "Brian", team, null);
        when(repo.save(isA(Player.class))).thenReturn(player);
        when(repo.findById(player.getId())).thenReturn(Optional.of(player));
        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));

        String uri = mockMvc.perform(post("/api/player")
                .content("{\"name\":\"Brian\", \"teamId\": \"1\"}")
                .contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost:8080/api/player/" + player.getId()))
                .andDo(document("player/create"))
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo("Brian")))
                .andExpect(MockMvcResultMatchers.jsonPath("name", equalTo("Brian")))
                .andDo(document("player/get"));
    }

    @Test
    public void deletePlayer_shouldDeletePlayer() throws Exception {
        Team team = new Team(1L, "Brian's Bowlers!", new LinkedList<>(),null, null, new LinkedList<>());
        Player brian = new Player(1L, "Brian", team, null);
        Player dave = new Player(2L, "Dave", team, null);
        team.addPlayer(brian);
        team.addPlayer(dave);

        doNothing().when(repo).deleteById(isA(Long.class));
        when(repo.findById(brian.getId())).thenReturn(Optional.of(brian));

        mockMvc.perform(delete("/api/player/1"))
                .andExpect(status().isNoContent())
                .andDo(document("player/delete"));
        verify(repo, times(1)).deleteById(1L);
        assertThat(team.getPlayers()).doesNotContain(brian);
        assertThat(team.getPlayers()).contains(dave);
    }
}