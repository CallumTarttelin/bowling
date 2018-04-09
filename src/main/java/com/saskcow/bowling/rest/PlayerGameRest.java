package com.saskcow.bowling.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerGameRest {
    private List<Long> playerIds;
    private Long teamId;
    private Long gameId;
}
