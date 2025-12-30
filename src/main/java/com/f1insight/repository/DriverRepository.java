package com.f1insight.repository;

import com.f1insight.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    /**
     * İsme göre driver bulma
     * 
     * @param name Driver ismi
     * @return Optional Driver
     */
    Optional<Driver> findByName(String name);

    /**
     * Takıma göre driver'ları listeleme
     * 
     * @param team Takım adı
     * @return Driver listesi
     */
    List<Driver> findByTeam(String team);

    /**
     * Ülkeye göre driver'ları listeleme
     * 
     * @param country Ülke adı
     * @return Driver listesi
     */
    List<Driver> findByCountry(String country);

    /**
     * Puan sayısına göre sıralanmış driver'ları getirme
     * 
     * @return Puanlarına göre azalan sırada driver listesi
     */
    List<Driver> findAllByOrderByPointsDesc();

    /**
     * Galibiyet sayısına göre sıralanmış driver'ları getirme
     * 
     * @return Galibiyetlerine göre azalan sırada driver listesi
     */
    List<Driver> findAllByOrderByWinsDesc();

    /**
     * Belirli puan üzerindeki driver'ları getirme
     * 
     * @param points Minimum puan
     * @return Driver listesi
     */
    List<Driver> findByPointsGreaterThanEqual(int points);

    /**
     * Belirli galibiyet sayısı üzerindeki driver'ları getirme
     * 
     * @param wins Minimum galibiyet sayısı
     * @return Driver listesi
     */
    List<Driver> findByWinsGreaterThanEqual(int wins);

    /**
     * İsim içeren driver'ları arama (case-insensitive)
     * 
     * @param name Aranacak isim parçası
     * @return Driver listesi
     */
    List<Driver> findByNameContainingIgnoreCase(String name);

    /**
     * Driver'ı yarış sonuçlarıyla birlikte getirme (N+1 problemi önleme)
     * 
     * @param id Driver ID
     * @return Optional Driver with race results
     */
    @Query("SELECT d FROM Driver d LEFT JOIN FETCH d.lastTenRaces WHERE d.id = :id")
    Optional<Driver> findByIdWithRaceResults(@Param("id") Long id);

    /**
     * Tüm driver'ları yarış sonuçlarıyla birlikte getirme
     * 
     * @return Driver listesi with race results
     */
    @Query("SELECT DISTINCT d FROM Driver d LEFT JOIN FETCH d.lastTenRaces")
    List<Driver> findAllWithRaceResults();
}
