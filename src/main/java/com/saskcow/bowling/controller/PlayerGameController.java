package com.saskcow.bowling.controller;

import com.saskcow.bowling.repository.GameRepository;
import com.saskcow.bowling.repository.PlayerGameRepository;
import com.saskcow.bowling.repository.PlayerRepository;
import com.saskcow.bowling.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class PlayerGameController {

    private PlayerGameRepository repo;
    private PlayerRepository playerRepository;
    private TeamRepository teamRepository;
    private GameRepository gameRepository;

    @Autowired
    public PlayerGameController(PlayerGameRepository repo, PlayerRepository playerRepository, TeamRepository teamRepository, GameRepository gameRepository){
        this.repo = repo;
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.gameRepository = gameRepository;
    }
}
