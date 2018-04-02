package com.saskcow.bowling.controller;


import com.saskcow.bowling.domain.League;
import com.saskcow.bowling.domain.Rota;
import com.saskcow.bowling.domain.Team;
import com.saskcow.bowling.repository.LeagueRepository;
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
public class TeamControllerTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("reports/java-snippets");

    @Mock
    private TeamRepository repo;
    @Mock
    private LeagueRepository leagueRepository;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TeamController(repo, leagueRepository))
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Test
    public void addTeam_shouldSaveTheTeam() throws Exception {
        League league = new League(1L, "Brian", new Rota(),  new LinkedList<>());
        Team team = new Team(1L, "Brian", null, null, league, new LinkedList<>());
        when(repo.save(isA(Team.class))).thenReturn(team);
        when(repo.findById(team.getId())).thenReturn(Optional.of(team));
        when(leagueRepository.findById(league.getId())).thenReturn(Optional.of(league));

        String uri = mockMvc.perform(post("/api/team")
                .content("{\"name\":\"Brian\", \"leagueId\": \"1\"}")
                .contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost:8080/api/team/" + team.getId()))
                .andDo(document("team/create"))
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo("Brian")))
                .andExpect(MockMvcResultMatchers.jsonPath("name", equalTo("Brian")))
                .andDo(document("team/get"));
    }

    @Test
    public void deleteTeam_shouldDeleteTeam() throws Exception {
        League league = new League(1L, "Brian", new Rota(), new LinkedList<>());
        Team team = new Team(1L, "Brian", null, null, league, new LinkedList<>());
        Team team2 = new Team(2L, "Dave", null, null, league, new LinkedList<>());
        league.addTeam(team);
        league.addTeam(team2);

        doNothing().when(repo).deleteById(isA(Long.class));
        when(repo.findById(team.getId())).thenReturn(Optional.of(team));

        mockMvc.perform(delete("/api/team/1"))
                .andDo(document("team/delete"))
                .andExpect(status().isNoContent());
        verify(repo, times(1)).deleteById(1L);
        assertThat(league.getTeams()).doesNotContain(team);
        assertThat(league.getTeams()).contains(team2);
    }
}