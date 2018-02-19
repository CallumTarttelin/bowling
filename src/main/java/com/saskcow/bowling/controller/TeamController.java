package com.saskcow.bowling.controller;

import com.saskcow.bowling.domain.League;
import com.saskcow.bowling.domain.Team;
import com.saskcow.bowling.repository.LeagueRepository;
import com.saskcow.bowling.repository.TeamRepository;
import com.saskcow.bowling.rest.TeamRest;
import com.saskcow.bowling.view.TeamView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Controller
public class TeamController {

    private TeamRepository repo;
    private LeagueRepository leagueRepository;

    @Autowired
    public TeamController(TeamRepository repo, LeagueRepository leagueRepository){
        this.repo = repo;
        this.leagueRepository = leagueRepository;
    }

    @RequestMapping(value = "/api/team/{id}", method = RequestMethod.GET)
    public ResponseEntity<TeamView> findLeague(@PathVariable("id") Long id) {
        Team team = repo.findOne(id);
        TeamView teamView = new TeamView(team);
        return ResponseEntity.ok(teamView);
    }

    @RequestMapping(value = "/api/team", method = RequestMethod.POST)
    public ResponseEntity<?> saveLeague(@RequestBody TeamRest team) {
        League league = leagueRepository.findOne(team.getLeagueId());
        Team savedTeam = repo.save(new Team(team.getName(), league));
        league.addTeam(savedTeam);
        leagueRepository.save(league);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedTeam.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @RequestMapping(value = "/api/team/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteLeague(@PathVariable("id") Long id) {
        try {
            Team team = repo.findOne(id);
            team.getLeague().deleteTeam(team);
            repo.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
