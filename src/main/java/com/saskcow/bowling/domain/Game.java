package com.saskcow.bowling.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
@ToString(exclude="teams")
public class Game {
    private @Id @GeneratedValue Long id;
    private LocalDateTime time;
    private String venue;
    @ManyToMany
    private List<Team> teams;

    public Game(LocalDateTime time, String venue, List<Team> teams) {
        this.time = time;
        this.venue = venue;
        this.teams = teams;
    }
}
