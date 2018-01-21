package com.saskcow.bowling.controller;


import com.saskcow.bowling.domain.League;
import com.saskcow.bowling.repository.LeagueRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
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
        League league = new League(1L, "Brian", null, null);
        when(repo.save(isA(League.class))).thenReturn(league);
        when(repo.findOne(league.getId())).thenReturn(league);

        mockMvc.perform(post("/api/league")
                .content("{\"name\":\"Brian\"}")
                .contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/league/" + league.getId()));

        mockMvc.perform(get("/api/league"))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)));
//                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasItem("Brian")));

        mockMvc.perform(get("/api/league/" + league.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("name", equalTo("Brian")));

    }
}