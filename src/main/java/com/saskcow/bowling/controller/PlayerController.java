package com.saskcow.bowling.controller;

import com.saskcow.bowling.domain.Player;
import com.saskcow.bowling.domain.Team;
import com.saskcow.bowling.repository.PlayerRepository;
import com.saskcow.bowling.repository.TeamRepository;
import com.saskcow.bowling.rest.PlayerRest;
import com.saskcow.bowling.view.PlayerView;
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
public class PlayerController {

    private PlayerRepository repo;
    private TeamRepository teamRepository;

    @Autowired
    public PlayerController(PlayerRepository repo, TeamRepository teamRepository){
        this.repo = repo;
        this.teamRepository = teamRepository;
    }

    @RequestMapping(value = "/api/player/{id}", method = RequestMethod.GET)
    public ResponseEntity<PlayerView> findLeague(@PathVariable("id") Long id) {
        Player player = repo.findOne(id);
        PlayerView playerView = new PlayerView(player);
        return ResponseEntity.ok(playerView);
    }

    @RequestMapping(value = "/api/player", method = RequestMethod.POST)
    public ResponseEntity<?> saveLeague(@RequestBody PlayerRest player) {
        Team team = teamRepository.findOne(player.getTeamId());
        Player savedPlayer = repo.save(new Player(player.getName(), team));
        team.addPlayer(savedPlayer);
        teamRepository.save(team);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedPlayer.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @RequestMapping(value = "/api/player/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteLeague(@PathVariable("id") Long id) {
        try {
            Player player = repo.findOne(id);
            player.getTeam().deletePlayer(player);
            repo.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}