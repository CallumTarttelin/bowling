package com.saskcow.bowling.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerGameRest {
    private Long playerId;
    private Long teamId;
    private Long gameId;
}
