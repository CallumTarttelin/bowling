package com.saskcow.bowling.controller;

import com.saskcow.bowling.domain.League;
import com.saskcow.bowling.domain.Rota;
import com.saskcow.bowling.repository.LeagueRepository;
import com.saskcow.bowling.repository.RotaRepository;
import com.saskcow.bowling.view.LeagueView;
import com.saskcow.bowling.view.LeagueViewSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class LeagueController {

    private LeagueRepository repo;
    private RotaRepository rotaRepository;

    @Autowired
    public LeagueController(LeagueRepository repo, RotaRepository rotaRepository){
        this.repo = repo;
        this.rotaRepository = rotaRepository;
    }

    @RequestMapping(value = "/api/league", method = RequestMethod.GET)
    public ResponseEntity<List<LeagueViewSummary>> findLeagues() {
        List<LeagueViewSummary> leagues = StreamSupport.stream(repo.findAll().spliterator(), false)
                .map(LeagueViewSummary::new).collect(Collectors.toList());
        return ResponseEntity.ok(leagues);
    }

    @RequestMapping(value = "/api/league", method = RequestMethod.GET, params = "name")
    public ResponseEntity<List<LeagueViewSummary>> findLeaguesByName(@RequestParam("name") String name) {
        List<LeagueViewSummary> leagues = repo.findByNameContaining(name).stream().map(LeagueViewSummary::new).collect(Collectors.toList());
        return ResponseEntity.ok(leagues);
    }

    @RequestMapping(value = "/api/league/{id}", method = RequestMethod.GET)
    public ResponseEntity<LeagueView> findLeague(@PathVariable("id") Long id) {
        League league = repo.findOne(id);
        LeagueView leagueView = new LeagueView(league);
        return ResponseEntity.ok(leagueView);
    }

    @RequestMapping(value = "/api/league", method = RequestMethod.POST)
    public ResponseEntity<?> saveLeague(@RequestBody League league) {
        if(league.getTeams() == null){
            league.setTeams(new LinkedList<>());
        }
        League savedLeague = repo.save(league);
        Rota rota = new Rota(league);
        rotaRepository.save(rota);
        savedLeague.setRota(rota);
        repo.save(savedLeague);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedLeague.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @RequestMapping(value = "/api/league/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteLeague(@PathVariable("id") Long id) {
        try {
            repo.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
