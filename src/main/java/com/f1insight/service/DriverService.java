package com.f1insight.service;

import com.f1insight.model.Driver;
import com.f1insight.repository.DriverRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {

        private final DriverRepository driverRepository;

        public DriverService(DriverRepository driverRepository) {
                this.driverRepository = driverRepository;
        }

        /**
         * Tüm pilotları puana göre sıralı getir
         */
        public List<Driver> getAllDrivers() {
                return driverRepository.findAllByOrderByPointsDesc();
        }

        /**
         * ID'ye göre pilot getir
         */
        public Driver getDriverById(Long id) {
                return driverRepository.findById(id).orElse(null);
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
