package com.saskcow.bowling.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamRest {
    private String name;
    private Long leagueId;
}
