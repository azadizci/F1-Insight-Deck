package com.f1insight.repository;

import com.f1insight.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    /**
     * İsme göre takım bulma
     * 
     * @param name Takım ismi
     * @return Optional Team
     */
    Optional<Team> findByName(String name);

    /**
     * Takımı pilotlarıyla birlikte getir (N+1 problemi önleme)
     * 
     * @param id Team ID
     * @return Optional Team with drivers
     */
    @Query("SELECT t FROM Team t LEFT JOIN FETCH t.drivers WHERE t.id = :id")
    Optional<Team> findByIdWithDrivers(@Param("id") Long id);

    /**
     * Tüm takımları pilotlarıyla birlikte getir
     * 
     * @return Team listesi with drivers
     */
    @Query("SELECT DISTINCT t FROM Team t LEFT JOIN FETCH t.drivers")
    List<Team> findAllWithDrivers();

    /**
     * İsme göre takımı pilotlarıyla birlikte getir
     * 
     * @param name Takım ismi
     * @return Optional Team with drivers
     */
    @Query("SELECT t FROM Team t LEFT JOIN FETCH t.drivers WHERE t.name = :name")
    Optional<Team> findByNameWithDrivers(@Param("name") String name);
}
