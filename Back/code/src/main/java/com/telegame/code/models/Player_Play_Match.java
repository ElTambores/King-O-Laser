package com.telegame.code.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Player_Play_Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "player_id")
    Player player;

    @ManyToOne
    @JoinColumn(name = "game_match_id")
    GameMatch gameMatch;

    LocalDateTime matchCreation;
}