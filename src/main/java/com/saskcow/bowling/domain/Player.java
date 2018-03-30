package com.saskcow.bowling.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private @Id @GeneratedValue Long id;
    private String name;
    @ManyToOne
    private Team team;

    public Player(String name, Team team) {
        this.name = name;
        this.team = team;
    }
}
