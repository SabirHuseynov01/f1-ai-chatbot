package com.example.f1aichatbot.controller;

import com.example.f1aichatbot.model.Driver;
import com.example.f1aichatbot.model.Race;
import com.example.f1aichatbot.model.Team;
import com.example.f1aichatbot.service.F1DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/f1")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class F1DataController {

    private final F1DataService f1DataService;

     // ===================
     //  Driver Endpoints
     // ===================

    @GetMapping("/drivers")
    public ResponseEntity<List<Driver>> getActiveDrivers(){
        return ResponseEntity.ok(f1DataService.getAllActiveDrivers());
    }

    @GetMapping("/drivers/search")
    public ResponseEntity<List<Driver>> searchDrivers(@RequestParam String query) {
        return ResponseEntity.ok(f1DataService.searchDrivers(query));
    }

    @GetMapping("/drivers/team/{teamName}")
    public ResponseEntity<List<Driver>> getDriversByTeam(@PathVariable String teamName) {
        return ResponseEntity.ok(f1DataService.getDriversByTeam(teamName));
    }

    // ===========================
    // Team Endpoints
    // ===========================

    @GetMapping("/teams")
    public ResponseEntity<List<Team>> getActiveTeams() {
        return ResponseEntity.ok(f1DataService.getAllActiveTeams());
    }

    @GetMapping("/teams/standings")
    public ResponseEntity<List<Team>> getTeamStandings() {
        return ResponseEntity.ok(f1DataService.getTeamsOrderedByChampionships());
    }

    // ===========================
    // Race Endpoints
    // ===========================

    @GetMapping("/races/season/{year}")
    public ResponseEntity<List<Race>> getRacesBySeason(@PathVariable int year) {
        return ResponseEntity.ok(f1DataService.getRacesBySeason(year));
    }

    @GetMapping("/races/upcoming")
    public ResponseEntity<List<Race>> getUpcomingRaces() {
        return ResponseEntity.ok(f1DataService.getUpcomingRaces());
    }

    @GetMapping("/races/seasons")
    public ResponseEntity<List<Integer>> getAllSeasons() {
        return ResponseEntity.ok(f1DataService.getAllSeasons());
    }

}


