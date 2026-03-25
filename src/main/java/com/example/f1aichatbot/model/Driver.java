package com.example.f1aichatbot.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "drivers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nationality;

    @Column(name = "driver_number")
    private Integer driverNumber;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "world_championships")
    private Integer worldChampionships;

    @Column(name = "career_wins")
    private Integer careerWins;

    @Column(name = "career_podiums")
    private Integer careerPodiums;

    @Column(name = "career_poles")
    private Integer careerPoles;

    @Column(name = "career_points")
    private Double careerPoints;

    @Column(name = "debut_year")
    private Integer debutYear;

    @Column(length = 1000)
    private String biography;

    @Column(name = "is_active")
    private Boolean isActive;
}
