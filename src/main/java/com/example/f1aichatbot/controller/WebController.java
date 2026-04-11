package com.example.f1aichatbot.controller;

import com.example.f1aichatbot.service.F1DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final F1DataService f1DataService;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(required = false) String session) {
        String sessionId = (session != null && !session.isBlank())
                ? session
                : "session-" + UUID.randomUUID().toString().substring(0, 8);

        model.addAttribute("sessionId", sessionId);
        model.addAttribute("activeDriverCount", f1DataService.getAllActiveDrivers().size());
        model.addAttribute("activeTeamCount", f1DataService.getAllActiveTeams().size());
        model.addAttribute("upcomingRaces", f1DataService.getUpcomingRaces());
        return "index";
    }

    @GetMapping("/drivers")
    public String drivers(Model model) {
        model.addAttribute("drivers", f1DataService.getAllActiveDrivers());
        model.addAttribute("champions", f1DataService.getChampions());
        return "drivers";
    }

    @GetMapping("/teams")
    public String teams(Model model) {
        model.addAttribute("teams", f1DataService.getAllActiveTeams());
        return "teams";
    }

    @GetMapping("/schedule")
    public String schedule(Model model) {
        model.addAttribute("races2025", f1DataService.getRacesBySeason(2025));
        model.addAttribute("upcomingRaces", f1DataService.getUpcomingRaces());
        return "schedule";
    }
}



