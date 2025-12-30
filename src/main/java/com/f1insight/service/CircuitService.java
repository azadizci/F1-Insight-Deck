package com.f1insight.service;

import com.f1insight.model.Circuit;
import com.f1insight.model.Race;
import com.f1insight.repository.CircuitRepository;
import com.f1insight.repository.RaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CircuitService {

    private final CircuitRepository circuitRepository;
    private final RaceRepository raceRepository;

    public CircuitService(CircuitRepository circuitRepository, RaceRepository raceRepository) {
        this.circuitRepository = circuitRepository;
        this.raceRepository = raceRepository;
    }

    /**
     * 2024 yarış takvimini getir
     */
    @Transactional(readOnly = true)
    public List<Race> get2024RaceCalendar() {
        return raceRepository.findBySeasonWithCircuit(2024);
    }

    /**
     * Round'a göre yarış getir
     */
    @Transactional(readOnly = true)
    public Race getRaceByRound(int round) {
        return raceRepository.findBySeasonAndRound(2024, round).orElse(null);
    }

    /**
     * ID'ye göre circuit getir
     */
    @Transactional(readOnly = true)
    public Circuit getCircuitById(Long id) {
        return circuitRepository.findById(id).orElse(null);
    }

    /**
     * CircuitId'ye göre circuit getir
     */
    @Transactional(readOnly = true)
    public Circuit getCircuitByCircuitId(String circuitId) {
        return circuitRepository.findByCircuitId(circuitId).orElse(null);
    }

    /**
     * Tüm pistleri getir
     */
    @Transactional(readOnly = true)
    public List<Circuit> getAllCircuits() {
        return circuitRepository.findAll();
    }
}
