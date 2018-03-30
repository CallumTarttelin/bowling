package com.saskcow.bowling.controller;


import com.saskcow.bowling.domain.League;
import com.saskcow.bowling.domain.Rota;
import com.saskcow.bowling.domain.Team;
import com.saskcow.bowling.repository.LeagueRepository;
import com.saskcow.bowling.repository.RotaRepository;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class LeagueControllerTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("reports/java-snippets");

    @Mock
    private LeagueRepository repo;
    @Mock
    private RotaRepository rotaRepository;
    private MockMvc mockMvc;

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(new LeagueController(repo, rotaRepository))
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Test
    public void addLeague_shouldSaveTheLeague() throws Exception {
        League league = new League(1L, "Brian", new Rota(), new LinkedList<>());
        league.getRota().setLeague(league);
        Team team = new Team("Team Brian", league);
        league.addTeam(team);
        when(repo.save(isA(League.class))).thenReturn(league);
        when(repo.findById(league.getId())).thenReturn(Optional.of(league));
        when(repo.findAll()).thenReturn(Collections.singletonList(league));

        String uri = mockMvc.perform(post("/api/league")
                .content("{\"name\":\"Brian\"}")
                .contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost:8080/api/league/" + league.getId()))
                .andDo(document("league/create"))
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(get("/api/league"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", equalTo("Brian")))
                .andDo(document("league/many"));

        mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo("Brian")))
                .andExpect(MockMvcResultMatchers.jsonPath("name", equalTo("Brian")))
                .andDo(document("league/single"));

    }

    @Test
    public void getLeague_shouldFilter() throws Exception {
        League dave = new League(1L, "Dave", new Rota(), null);
        League david = new League(2L, "David", new Rota(), null);
        League brian = new League(3L, "Brian", new Rota(), null);
        when(repo.findByNameContaining("Dav")).thenReturn(Arrays.asList(dave, david));
        when(repo.findByNameContaining("Bri")).thenReturn(Collections.singletonList(brian));

        mockMvc.perform(get("/api/league?name=Dav"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", equalTo("Dave")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", equalTo("David")));

        mockMvc.perform(get("/api/league?name=Bri"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", equalTo("Brian")))
                .andDo(document("league/filter"));
    }

    @Test
    public void deleteLeague_shouldDeleteLeague() throws Exception {
        doNothing().when(repo).deleteById(isA(Long.class));

        mockMvc.perform(delete("/api/league/1"))
                .andExpect(status().isNoContent())
                .andDo(document("league/delete"));
        verify(repo, times(1)).deleteById(1L);
        }
}