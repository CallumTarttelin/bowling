package com.saskcow.bowling.controller;


import com.saskcow.bowling.domain.League;
import com.saskcow.bowling.repository.LeagueRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class LeagueControllerTest {

    @Mock
    private LeagueRepository repo;
    private MockMvc mockMvc;

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(new LeagueController(repo)).build();
    }

    @Test
    public void addLeague_shouldSaveTheCourse() throws Exception {
        League result = new League(3L,"Brian", null, null);
        when(repo.save(isA(League.class))).thenReturn(result);
        when(repo.findOne(result.getId())).thenReturn(result);

        mockMvc.perform(post("/api/league")
                .content("{\"name\":\"Brian\"}")
                .contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/league/" + result.getId()));

        mockMvc.perform(get("/api/leagues/{id}", "id").param("id", result.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("name", equalTo("Some Course")));

        mockMvc.perform(get("/api/leagues"))
                .andExpect((MockMvcResultMatchers.jsonPath("$.data", Matchers.containsInAnyOrder("{\"name\":\"Brian\", \"games\": null, \"teams\": null}"))));

    }
}