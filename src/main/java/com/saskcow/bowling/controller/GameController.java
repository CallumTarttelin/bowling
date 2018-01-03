package com.saskcow.bowling.controller;

import org.springframework.beans.factory.annotation.Value;

public class GameController {
    @Value("${spring.data.rest.base-path}")
    private String basePath;


}
