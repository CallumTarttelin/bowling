package com.saskcow.bowling.controller;


import com.saskcow.bowling.domain.League;
import com.saskcow.bowling.domain.Team;
import com.saskcow.bowling.repository.LeagueRepository;
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

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TeamControllerTest {

    @Mock
    private TeamRepository repo;
    @Mock
    private LeagueRepository leagueRepository;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TeamController(repo, leagueRepository)).build();
    }

    @Test
    public void addTeam_shouldSaveTheTeam() throws Exception {
        League league = new League(1L, "Brian",  new LinkedList<>());
        Team team = new Team(1L, "Brian", null, null, league);
        when(repo.save(isA(Team.class))).thenReturn(team);
        when(repo.findOne(team.getId())).thenReturn(team);
        when(repo.findAll()).thenReturn(Collections.singletonList(team));
        when(leagueRepository.findOne(league.getId())).thenReturn(league);

        String uri = mockMvc.perform(post("/api/team")
                .content("{\"name\":\"Brian\", \"leagueId\": \"1\"}")
                .contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/team/" + team.getId()))
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo("Brian")))
                .andExpect(MockMvcResultMatchers.jsonPath("name", equalTo("Brian")));
    }

    @Test
    public void deleteTeam_shouldDeleteTeam() throws Exception {
        League league = new League(1L, "Brian", new LinkedList<>());
        Team team = new Team(1L, "Brian", null, null, league);
        Team team2 = new Team(2L, "Dave", null, null, league);
        league.addTeam(team);
        league.addTeam(team2);

        doNothing().when(repo).delete(isA(Long.class));
        when(repo.findOne(team.getId())).thenReturn(team);

        mockMvc.perform(delete("/api/team/1"))
                .andExpect(status().isNoContent());
        verify(repo, times(1)).delete(1L);
        assertThat(league.getTeams()).doesNotContain(team);
        assertThat(league.getTeams()).contains(team2);
    }
}