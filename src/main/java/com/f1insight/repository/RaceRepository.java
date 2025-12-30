package com.f1insight.repository;

import com.f1insight.model.Race;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RaceRepository extends JpaRepository<Race, Long> {

    /**
     * Sezona göre yarışları round sırasına göre getir
     * 
     * @param season Sezon yılı
     * @return Race listesi
     */
    List<Race> findBySeasonOrderByRoundAsc(Integer season);

    /**
     * Sezon ve round'a göre yarış bul
     * 
     * @param season Sezon yılı
     * @param round  Yarış sırası
     * @return Optional Race
     */
    Optional<Race> findBySeasonAndRound(Integer season, Integer round);

    /**
     * Sezon ve piste göre yarış bul
     * 
     * @param season    Sezon yılı
     * @param circuitId Pist ID'si
     * @return Optional Race
     */
    @Query("SELECT r FROM Race r WHERE r.season = :season AND r.circuit.circuitId = :circuitId")
    Optional<Race> findBySeasonAndCircuitId(@Param("season") Integer season, @Param("circuitId") String circuitId);

    /**
     * Yarışı circuit ile birlikte getir
     * 
     * @param season Sezon yılı
     * @return Race listesi with circuits
     */
    @Query("SELECT r FROM Race r LEFT JOIN FETCH r.circuit WHERE r.season = :season ORDER BY r.round ASC")
    List<Race> findBySeasonWithCircuit(@Param("season") Integer season);
}
