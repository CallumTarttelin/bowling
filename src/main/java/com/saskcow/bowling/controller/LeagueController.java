package com.saskcow.bowling.controller;

import com.saskcow.bowling.domain.League;
import com.saskcow.bowling.repository.LeagueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LeagueController {

    private LeagueRepository repo;

    @Autowired
    public LeagueController(LeagueRepository repo){
        this.repo = repo;
    }

    @RequestMapping(value = "/api/league", method = RequestMethod.GET)
    public ResponseEntity<List<League>> findLeagues() {
        List<League> leagues = new ArrayList<>();
        repo.findAll().forEach(leagues::add);
        return ResponseEntity.ok(leagues);
    }

    @RequestMapping(value = "/api/league/{id}", method = RequestMethod.GET)
    public ResponseEntity<League> findLeague(@RequestParam("id") Long id) {
        League league = repo.findOne(id);
        return ResponseEntity.ok(league);
    }

    @RequestMapping(value = "/api/league", method = RequestMethod.POST)
    public ResponseEntity<?> saveLeague(@RequestBody League league) {
        League savedLeague = repo.save(league);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedLeague.getId()).toUri();
        return ResponseEntity.created(location).build();
    }


}
