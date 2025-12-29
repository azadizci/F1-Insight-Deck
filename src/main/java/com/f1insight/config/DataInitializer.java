package com.f1insight.config;

import com.f1insight.service.F1DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner initData(F1DataService f1DataService) {
        return args -> {
            logger.info("========== VERİ YÜKLEMESİ BAŞLIYOR ==========");
            // Uygulama başladığında 2024 verilerini çek
            f1DataService.fetchAndSaveDriverStandings(2024);
            logger.info("========== VERİ YÜKLEMESİ TAMAMLANDI ==========");
        };
    }
}
