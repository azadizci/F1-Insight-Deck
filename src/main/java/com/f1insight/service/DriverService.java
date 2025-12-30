package com.f1insight.service;

import com.f1insight.model.Driver;
import com.f1insight.repository.DriverRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {

        private static final Logger logger = LoggerFactory.getLogger(DriverService.class);

        private final DriverRepository driverRepository;
        private final F1DataService f1DataService;

        public DriverService(DriverRepository driverRepository, F1DataService f1DataService) {
                this.driverRepository = driverRepository;
                this.f1DataService = f1DataService;
        }

        /**
         * Tüm pilotları puana göre sıralı getir
         * Eğer veritabanı boşsa, otomatik olarak F1 API'den veri yükler (lazy loading)
         */
        public List<Driver> getAllDrivers() {
                List<Driver> drivers = driverRepository.findAllByOrderByPointsDesc();

                // Eğer veritabanı boşsa, veri yüklemesi yap
                if (drivers.isEmpty()) {
                        logger.info("Veritabanında pilot bulunamadı. F1 API'den veri yükleniyor...");
                        f1DataService.fetchAndSaveDriverStandings(2024);
                        drivers = driverRepository.findAllByOrderByPointsDesc();
                        logger.info("{} pilot başarıyla yüklendi.", drivers.size());
                }

                return drivers;
        }

        /**
         * ID'ye göre pilot getir (yarış sonuçlarıyla birlikte)
         */
        public Driver getDriverById(Long id) {
                return driverRepository.findByIdWithRaceResults(id).orElse(null);
        }

        /**
         * İsme göre pilot getir
         */
        public Driver getDriverByName(String name) {
                return driverRepository.findByName(name).orElse(null);
        }

        /**
         * Takıma göre pilotları getir
         */
        public List<Driver> getDriversByTeam(String team) {
                return driverRepository.findByTeam(team);
        }
}
