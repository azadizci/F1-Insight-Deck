package com.f1insight.repository;

import com.f1insight.model.RaceResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * RaceResult entity için Spring Data JPA Repository interface'i.
 * JpaRepository'den extend ederek temel CRUD operasyonlarını otomatik olarak
 * sağlar.
 */
@Repository
public interface RaceResultRepository extends JpaRepository<RaceResult, Long> {

    /**
     * Driver ID'ye göre yarış sonuçlarını getirme
     * 
     * @param driverId Driver ID
     * @return RaceResult listesi
     */
    List<RaceResult> findByDriverId(Long driverId);

    /**
     * Yarış adına göre sonuçları getirme
     * 
     * @param raceName Yarış adı
     * @return RaceResult listesi
     */
    List<RaceResult> findByRaceName(String raceName);

    /**
     * Yarış adına göre sonuçları pozisyona göre sıralı getirme
     * 
     * @param raceName Yarış adı
     * @return Pozisyona göre sıralı RaceResult listesi
     */
    List<RaceResult> findByRaceNameOrderByPositionAsc(String raceName);

    /**
     * Belirli pozisyondaki sonuçları getirme
     * 
     * @param position Pozisyon
     * @return RaceResult listesi
     */
    List<RaceResult> findByPosition(int position);

    /**
     * Podyum (1., 2., 3.) sonuçlarını getirme
     * 
     * @return Podyum sonuçları listesi
     */
    List<RaceResult> findByPositionLessThanEqual(int position);

    /**
     * Bir driver'ın galibiyetlerini getirme
     * 
     * @param driverId Driver ID
     * @return Galibiyet sonuçları listesi
     */
    List<RaceResult> findByDriverIdAndPosition(Long driverId, int position);

    /**
     * Bir driver'ın podyum sonuçlarını getirme
     * 
     * @param driverId    Driver ID
     * @param maxPosition Maximum pozisyon (podyum için 3)
     * @return Podyum sonuçları listesi
     */
    List<RaceResult> findByDriverIdAndPositionLessThanEqual(Long driverId, int maxPosition);

    /**
     * Driver'a göre yarış sonuçlarını pozisyona göre sıralı getirme
     * 
     * @param driverId Driver ID
     * @return Pozisyona göre sıralı RaceResult listesi
     */
    List<RaceResult> findByDriverIdOrderByPositionAsc(Long driverId);

    /**
     * Belirli bir driver'ın galibiyet sayısını getirme
     * 
     * @param driverId Driver ID
     * @return Galibiyet sayısı
     */
    @Query("SELECT COUNT(r) FROM RaceResult r WHERE r.driver.id = :driverId AND r.position = 1")
    Long countWinsByDriverId(@Param("driverId") Long driverId);

    /**
     * Belirli bir driver'ın podyum sayısını getirme
     * 
     * @param driverId Driver ID
     * @return Podyum sayısı
     */
    @Query("SELECT COUNT(r) FROM RaceResult r WHERE r.driver.id = :driverId AND r.position <= 3")
    Long countPodiumsByDriverId(@Param("driverId") Long driverId);

    /**
     * Yarış adı içeren sonuçları arama (case-insensitive)
     * 
     * @param raceName Aranacak yarış adı parçası
     * @return RaceResult listesi
     */
    List<RaceResult> findByRaceNameContainingIgnoreCase(String raceName);

    /**
     * Tüm yarış sonuçlarını driver bilgisiyle birlikte getirme
     * 
     * @return RaceResult listesi with driver info
     */
    @Query("SELECT r FROM RaceResult r JOIN FETCH r.driver")
    List<RaceResult> findAllWithDriver();
}
