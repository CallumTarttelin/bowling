package com.saskcow.bowling.rest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameRest {
    private String time;
    private String venue;
    private Long rotaId;
    private Long teamId1;
    private Long teamId2;
}
