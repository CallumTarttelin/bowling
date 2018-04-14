package com.saskcow.bowling.controller;

import com.saskcow.bowling.domain.League;
import com.saskcow.bowling.domain.Team;
import com.saskcow.bowling.repository.LeagueRepository;
import com.saskcow.bowling.repository.TeamRepository;
import com.saskcow.bowling.rest.TeamRest;
import com.saskcow.bowling.view.TeamView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

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
        Optional<Team> optionalTeam = repo.findById(id);
        if(! optionalTeam.isPresent()){
            return ResponseEntity.notFound().build();
        }
        Team team = optionalTeam.get();
        TeamView teamView = new TeamView(team);
        teamView.setPinsFor(repo.pinsFor(team.getId()));
        teamView.setPinsAgainst(repo.pinsAgainst(team.getId()));
        teamView.setHighHandicapGame(repo.highHandicapGame(team.getId()));
        teamView.setHighHandicapSeries(repo.highHandicapSeries(team.getId()));
        teamView.setTeamPoints(repo.teamPoints(team.getId()));
        teamView.setTotalPoints(repo.totalPoints(team.getId()));
        return ResponseEntity.ok(teamView);
    }

    @RequestMapping(value = "/api/team", method = RequestMethod.POST)
    public ResponseEntity<?> saveLeague(@RequestBody TeamRest team) {
        Optional<League> optionalLeague = leagueRepository.findById(team.getLeagueId());
        if(! optionalLeague.isPresent()){
            return ResponseEntity.notFound().build();
        }
        League league = optionalLeague.get();
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
        Optional<Team> optionalTeam = repo.findById(id);
        if(! optionalTeam.isPresent()){
            return ResponseEntity.notFound().build();
        }
        Team team = optionalTeam.get();
        team.getLeague().deleteTeam(team);
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
