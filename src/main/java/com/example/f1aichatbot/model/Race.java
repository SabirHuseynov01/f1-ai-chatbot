package com.example.f1aichatbot.model;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "races")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Race {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "circuit_name", nullable = false)
    private String circuitName;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String city;

    @Column(name = "race_date")
    private LocalDate raceDate;

    private Integer season;

    @Column(name = "round_number")
    private Integer roundNumber;

    @Column(name = "winner_name")
    private String winnerName;

    @Column(name = "winner_team")
    private String winnerTeam;

    @Column(name = "fastest_lap_driver")
    private String fastestLapDriver;

    @Column(name = "fastest_lap_time")
    private String fastestLapTime;

    @Column(name = "total_laps")
    private Integer totalLaps;

    @Column(name = "circuit_length_km")
    private Double circuitLengthKm;

    @Column(name = "is_completed")
    private Boolean isCompleted;
}




