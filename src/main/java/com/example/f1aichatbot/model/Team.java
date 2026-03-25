package com.example.f1aichatbot.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "teams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "full_name")
    private String fullName;

    @Column(nullable = false)
    private String nationality;

    @Column(name = "base_location")
    private String baseLocation;

    @Column(name = "constructor_championships")
    private Integer constructorChampionships;

    @Column(name = "race_wins")
    private Integer raceWins;

    @Column(name = "founded_year")
    private Integer foundedYear;

    @Column(name = "team_principal")
    private String teamPrincipal;

    @Column(name = "power_unit")
    private String powerUnit;

    @Column(length = 1000)
    private String description;

    @Column(name = "is_active")
    private Boolean isActive;
}
