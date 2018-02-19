package com.saskcow.bowling.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping(value = {"/league/*/add-team", "/league/*/add-game", "league/*", "/", "/add/league", "/league", "/player/*", "/team/*", "/team/*/add-player"})
    public String index(){
        return "index";
    }

}
