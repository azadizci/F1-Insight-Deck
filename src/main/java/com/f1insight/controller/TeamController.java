package com.f1insight.controller;

import com.f1insight.model.Team;
import com.f1insight.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    /**
     * Takımlar sayfası
     */
    @GetMapping("/teams")
    public String teams(Model model) {
        model.addAttribute("teams", teamService.getAllTeamsWithStats());
        model.addAttribute("activePage", "teams");
        return "teams";
    }

    /**
     * Takım detayları API endpoint
     */
    @GetMapping("/api/team/{id}")
    @ResponseBody
    public Team getTeam(@PathVariable Long id) {
        return teamService.getTeamById(id);
    }
}
