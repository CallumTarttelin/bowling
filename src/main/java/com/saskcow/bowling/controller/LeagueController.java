package com.saskcow.bowling.controller;

import com.saskcow.bowling.domain.League;
import com.saskcow.bowling.domain.Rota;
import com.saskcow.bowling.repository.LeagueRepository;
import com.saskcow.bowling.repository.RotaRepository;
import com.saskcow.bowling.repository.TeamRepository;
import com.saskcow.bowling.view.LeagueView;
import com.saskcow.bowling.view.LeagueViewSummary;
import com.saskcow.bowling.view.TeamView;
import com.saskcow.bowling.view.TeamViewSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class LeagueController {

    private LeagueRepository repo;
    private RotaRepository rotaRepository;
    private TeamRepository teamRepository;

    @Autowired
    public LeagueController(LeagueRepository repo, RotaRepository rotaRepository, TeamRepository teamRepository){
        this.repo = repo;
        this.rotaRepository = rotaRepository;
        this.teamRepository = teamRepository;
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
        Optional<League> optionalLeague = repo.findById(id);
        if(! optionalLeague.isPresent()){
            return ResponseEntity.notFound().build();
        }
        League league = optionalLeague.get();
        LeagueView leagueView = new LeagueView(league);
        leagueView.getTeams().forEach(team -> {
            team.setPinsFor(teamRepository.pinsFor(team.getId()));
            team.setPinsAgainst(teamRepository.pinsAgainst(team.getId()));
            team.setHighHandicapGame(teamRepository.highHandicapGame(team.getId()));
            team.setHighHandicapSeries(teamRepository.highHandicapSeries(team.getId()));
            team.setTeamPoints(teamRepository.teamPoints(team.getId()));
            team.setTotalPoints(teamRepository.totalPoints(team.getId()));
        });
        leagueView.getTeams().sort(Comparator.comparing(team -> team.getTotalPoints() != null ? -team.getTotalPoints() : 0));
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
            repo.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
