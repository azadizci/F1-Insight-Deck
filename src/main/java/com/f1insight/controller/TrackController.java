package com.f1insight.controller;

import com.f1insight.model.Race;
import com.f1insight.service.CircuitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TrackController {

    private final CircuitService circuitService;

    public TrackController(CircuitService circuitService) {
        this.circuitService = circuitService;
    }

    /**
     * Tracks sayfası - 2024 yarış takvimi
     */
    @GetMapping("/tracks")
    public String tracks(Model model) {
        model.addAttribute("races", circuitService.get2024RaceCalendar());
        model.addAttribute("activePage", "tracks");
        return "tracks";
    }

    /**
     * Yarış detayları API endpoint
     */
    @GetMapping("/api/race/{round}")
    @ResponseBody
    public Race getRace(@PathVariable int round) {
        return circuitService.getRaceByRound(round);
    }
}
