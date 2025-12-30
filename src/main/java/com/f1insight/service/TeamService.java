package com.f1insight.service;

import com.f1insight.model.Team;
import com.f1insight.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    /**
     * Tüm takımları pilotlarıyla birlikte ve hesaplanmış istatistiklerle getir
     */
    @Transactional(readOnly = true)
    public List<Team> getAllTeamsWithStats() {
        List<Team> teams = teamRepository.findAllWithDrivers();

        // Her takım için istatistikleri hesapla
        for (Team team : teams) {
            calculateTeamStats(team);
        }

        // Toplam puana göre sırala (azalan)
        teams.sort(Comparator.comparingInt(Team::getTotalPoints).reversed());

        return teams;
    }

    /**
     * ID'ye göre takım getir (pilotlarla birlikte)
     */
    @Transactional(readOnly = true)
    public Team getTeamById(Long id) {
        Team team = teamRepository.findByIdWithDrivers(id).orElse(null);
        if (team != null) {
            calculateTeamStats(team);
        }
        return team;
    }

    /**
     * İsme göre takım getir
     */
    @Transactional(readOnly = true)
    public Team getTeamByName(String name) {
        Team team = teamRepository.findByNameWithDrivers(name).orElse(null);
        if (team != null) {
            calculateTeamStats(team);
        }
        return team;
    }

    /**
     * Takım istatistiklerini hesapla (pilotların toplamı)
     */
    private void calculateTeamStats(Team team) {
        if (team.getDrivers() == null || team.getDrivers().isEmpty()) {
            team.setTotalPoints(0);
            team.setTotalWins(0);
            team.setTotalPodiums(0);
            return;
        }

        int totalPoints = team.getDrivers().stream()
                .mapToInt(driver -> driver.getPoints())
                .sum();

        int totalWins = team.getDrivers().stream()
                .mapToInt(driver -> driver.getWins())
                .sum();

        int totalPodiums = team.getDrivers().stream()
                .mapToInt(driver -> driver.getPodiums())
                .sum();

        team.setTotalPoints(totalPoints);
        team.setTotalWins(totalWins);
        team.setTotalPodiums(totalPodiums);
    }
}
