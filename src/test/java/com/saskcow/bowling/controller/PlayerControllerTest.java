package com.saskcow.bowling.controller;

import com.saskcow.bowling.domain.Player;
import com.saskcow.bowling.domain.Team;
import com.saskcow.bowling.repository.PlayerRepository;
import com.saskcow.bowling.repository.TeamRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.LinkedList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class PlayerControllerTest {

    @Mock
    private PlayerRepository repo;
    @Mock
    private TeamRepository teamRepository;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new PlayerController(repo, teamRepository)).build();
    }

    @Test
    public void addPlayer_shouldSaveThePlayer() throws Exception {
        Team team = new Team(1L, "Brian's Bowlers!", new LinkedList<>(),null, null);
        Player player = new Player(1L, "Brian", team);
        when(repo.save(isA(Player.class))).thenReturn(player);
        when(repo.findOne(player.getId())).thenReturn(player);
        when(repo.findAll()).thenReturn(Collections.singletonList(player));
        when(teamRepository.findOne(team.getId())).thenReturn(team);

        String uri = mockMvc.perform(post("/api/player")
                .content("{\"name\":\"Brian\", \"teamId\": \"1\"}")
                .contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/player/" + player.getId()))
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo("Brian")))
                .andExpect(MockMvcResultMatchers.jsonPath("name", equalTo("Brian")));
    }

    @Test
    public void deletePlayer_shouldDeletePlayer() throws Exception {
        Team team = new Team(1L, "Brian's Bowlers!", new LinkedList<>(),null, null);
        Player brian = new Player(1L, "Brian", team);
        Player dave = new Player(2L, "Dave", team);
        team.addPlayer(brian);
        team.addPlayer(dave);

        doNothing().when(repo).delete(isA(Long.class));
        when(repo.findOne(brian.getId())).thenReturn(brian);

        mockMvc.perform(delete("/api/player/1"))
                .andExpect(status().isNoContent());
        verify(repo, times(1)).delete(1L);
        assertThat(team.getPlayers()).doesNotContain(brian);
        assertThat(team.getPlayers()).contains(dave);
    }
}