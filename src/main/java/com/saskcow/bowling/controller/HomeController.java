package com.saskcow.bowling.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping(value = {"/league/*", "/", "/add", "/league"})
    public String index(){
        return "index";
    }

}
