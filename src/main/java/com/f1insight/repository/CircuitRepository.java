package com.f1insight.repository;

import com.f1insight.model.Circuit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CircuitRepository extends JpaRepository<Circuit, Long> {

    /**
     * CircuitId'ye göre pist bulma (API'den gelen ID)
     * 
     * @param circuitId Pist ID'si (örn: "monaco")
     * @return Optional Circuit
     */
    Optional<Circuit> findByCircuitId(String circuitId);
}
