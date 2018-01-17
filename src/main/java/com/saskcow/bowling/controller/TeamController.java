package com.saskcow.bowling.controller;

import com.saskcow.bowling.domain.Team;
import com.saskcow.bowling.repository.TeamRepository;
import com.saskcow.bowling.view.TeamView;
import com.saskcow.bowling.view.TeamViewSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TeamController {

    private TeamRepository repo;

    @Autowired
    public TeamController(TeamRepository repo){
        this.repo = repo;
    }

    @RequestMapping(value = "/api/team", method = RequestMethod.GET)
    public ResponseEntity<Iterable<TeamViewSummary>> findLeagues() {
        List<TeamViewSummary> teams = new ArrayList<>();
        repo.findAll().forEach(team -> teams.add(new TeamViewSummary((team))));
        return ResponseEntity.ok(teams);
    }

    @RequestMapping(value = "/api/team/{id}", method = RequestMethod.GET)
    public ResponseEntity<TeamView> findLeague(@PathVariable("id") Long id) {
        Team team = repo.findOne(id);
        TeamView teamView = new TeamView(team);
        return ResponseEntity.ok(teamView);
    }

    @RequestMapping(value = "/api/team", method = RequestMethod.POST)
    public ResponseEntity<?> saveLeague(@RequestBody Team team) {
        Team savedTeam = repo.save(team);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedTeam.getId()).toUri();
        return ResponseEntity.created(location).build();
    }


}
