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

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    public void addLeague_shouldSaveTheLeague() throws Exception {
        League league = new League(1L, "Brian", null);
        when(repo.save(isA(League.class))).thenReturn(league);
        when(repo.findOne(league.getId())).thenReturn(league);
        when(repo.findAll()).thenReturn(Collections.singletonList(league));

        String uri = mockMvc.perform(post("/api/league")
                .content("{\"name\":\"Brian\"}")
                .contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/league/" + league.getId()))
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(get("/api/league"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", equalTo("Brian")));

        mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo("Brian")))
                .andExpect(MockMvcResultMatchers.jsonPath("name", equalTo("Brian")));

    }

    @Test
    public void getLeague_shouldFilter() throws Exception {
        League dave = new League(1L, "Dave", null );
        League david = new League(2L, "David", null );
        League brian = new League(3L, "Brian", null);
        when(repo.findAll()).thenReturn(Arrays.asList(dave, david, brian));
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
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", equalTo("Brian")));
    }

    @Test
    public void deleteLeague_shouldDeleteLeague() throws Exception {
        League league = new League(1L, "Brian", new LinkedList<>());

        doNothing().when(repo).delete(isA(Long.class));

        mockMvc.perform(delete("/api/league/1"))
                .andExpect(status().isNoContent());
        verify(repo, times(1)).delete(1L);
        }
}