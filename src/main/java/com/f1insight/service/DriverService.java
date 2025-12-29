package com.f1insight.service;

import com.f1insight.model.Driver;
import com.f1insight.model.RaceResult;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DriverService {

    private final Map<Long, Driver> drivers = new HashMap<>();

    public DriverService() {
        initializeDrivers();
    }

    private void initializeDrivers() {
        // Max Verstappen - Red Bull
        Driver verstappen = new Driver(
                1L,
                "Max Verstappen",
                "Red Bull Racing",
                63,
                111,
                437,
                "https://media.formula1.com/content/dam/fom-website/drivers/2024Drivers/verstappen.jpg.img.1920.medium.jpg/1708344545893.jpg",
                "Netherlands",
                "🇳🇱",
                "https://media.formula1.com/content/dam/fom-website/teams/2024/red-bull-racing-logo.png.transform/2col/image.png",
                "#3671C6");
        verstappen.setLastTenRaces(Arrays.asList(
                new RaceResult("Abu Dhabi GP", 1),
                new RaceResult("Qatar GP", 1),
                new RaceResult("Las Vegas GP", 1),
                new RaceResult("Brazil GP", 1),
                new RaceResult("Mexico GP", 2),
                new RaceResult("USA GP", 3),
                new RaceResult("Singapore GP", 2),
                new RaceResult("Azerbaijan GP", 5),
                new RaceResult("Italy GP", 6),
                new RaceResult("Netherlands GP", 1)));
        drivers.put(1L, verstappen);

        // Lewis Hamilton - Ferrari (2025)
        Driver hamilton = new Driver(
                2L,
                "Lewis Hamilton",
                "Scuderia Ferrari",
                105,
                201,
                223,
                "https://media.formula1.com/content/dam/fom-website/drivers/2024Drivers/hamilton.jpg.img.1920.medium.jpg/1708344465302.jpg",
                "United Kingdom",
                "🇬🇧",
                "https://media.formula1.com/content/dam/fom-website/teams/2024/ferrari-logo.png.transform/2col/image.png",
                "#E80020");
        hamilton.setLastTenRaces(Arrays.asList(
                new RaceResult("Abu Dhabi GP", 4),
                new RaceResult("Qatar GP", 2),
                new RaceResult("Las Vegas GP", 2),
                new RaceResult("Brazil GP", 10),
                new RaceResult("Mexico GP", 4),
                new RaceResult("USA GP", 2),
                new RaceResult("Singapore GP", 3),
                new RaceResult("Azerbaijan GP", 9),
                new RaceResult("Italy GP", 5),
                new RaceResult("Netherlands GP", 8)));
        drivers.put(2L, hamilton);

        // Charles Leclerc - Ferrari
        Driver leclerc = new Driver(
                3L,
                "Charles Leclerc",
                "Scuderia Ferrari",
                7,
                40,
                356,
                "https://media.formula1.com/content/dam/fom-website/drivers/2024Drivers/leclerc.jpg.img.1920.medium.jpg/1708344363428.jpg",
                "Monaco",
                "🇲🇨",
                "https://media.formula1.com/content/dam/fom-website/teams/2024/ferrari-logo.png.transform/2col/image.png",
                "#E80020");
        leclerc.setLastTenRaces(Arrays.asList(
                new RaceResult("Abu Dhabi GP", 3),
                new RaceResult("Qatar GP", 4),
                new RaceResult("Las Vegas GP", 1),
                new RaceResult("Brazil GP", 5),
                new RaceResult("Mexico GP", 1),
                new RaceResult("USA GP", 1),
                new RaceResult("Singapore GP", 5),
                new RaceResult("Azerbaijan GP", 2),
                new RaceResult("Italy GP", 1),
                new RaceResult("Netherlands GP", 3)));
        drivers.put(3L, leclerc);

        // Lando Norris - McLaren
        Driver norris = new Driver(
                4L,
                "Lando Norris",
                "McLaren F1 Team",
                4,
                26,
                374,
                "https://media.formula1.com/content/dam/fom-website/drivers/2024Drivers/norris.jpg.img.1920.medium.jpg/1708344470980.jpg",
                "United Kingdom",
                "🇬🇧",
                "https://media.formula1.com/content/dam/fom-website/teams/2024/mclaren-logo.png.transform/2col/image.png",
                "#FF8000");
        norris.setLastTenRaces(Arrays.asList(
                new RaceResult("Abu Dhabi GP", 2),
                new RaceResult("Qatar GP", 3),
                new RaceResult("Las Vegas GP", 3),
                new RaceResult("Brazil GP", 2),
                new RaceResult("Mexico GP", 3),
                new RaceResult("USA GP", 4),
                new RaceResult("Singapore GP", 1),
                new RaceResult("Azerbaijan GP", 4),
                new RaceResult("Italy GP", 3),
                new RaceResult("Netherlands GP", 2)));
        drivers.put(4L, norris);

        // Carlos Sainz - Williams (2025)
        Driver sainz = new Driver(
                5L,
                "Carlos Sainz",
                "Williams Racing",
                4,
                25,
                290,
                "https://media.formula1.com/content/dam/fom-website/drivers/2024Drivers/sainz.jpg.img.1920.medium.jpg/1708344449073.jpg",
                "Spain",
                "🇪🇸",
                "https://media.formula1.com/content/dam/fom-website/teams/2024/williams-logo.png.transform/2col/image.png",
                "#64C4FF");
        sainz.setLastTenRaces(Arrays.asList(
                new RaceResult("Abu Dhabi GP", 5),
                new RaceResult("Qatar GP", 6),
                new RaceResult("Las Vegas GP", 4),
                new RaceResult("Brazil GP", 3),
                new RaceResult("Mexico GP", 1),
                new RaceResult("USA GP", 5),
                new RaceResult("Singapore GP", 4),
                new RaceResult("Azerbaijan GP", 6),
                new RaceResult("Italy GP", 4),
                new RaceResult("Netherlands GP", 5)));
        drivers.put(5L, sainz);

        // George Russell - Mercedes
        Driver russell = new Driver(
                6L,
                "George Russell",
                "Mercedes-AMG Petronas",
                3,
                16,
                245,
                "https://media.formula1.com/content/dam/fom-website/drivers/2024Drivers/russell.jpg.img.1920.medium.jpg/1708344472658.jpg",
                "United Kingdom",
                "🇬🇧",
                "https://media.formula1.com/content/dam/fom-website/teams/2024/mercedes-logo.png.transform/2col/image.png",
                "#27F4D2");
        russell.setLastTenRaces(Arrays.asList(
                new RaceResult("Abu Dhabi GP", 6),
                new RaceResult("Qatar GP", 5),
                new RaceResult("Las Vegas GP", 1),
                new RaceResult("Brazil GP", 4),
                new RaceResult("Mexico GP", 5),
                new RaceResult("USA GP", 6),
                new RaceResult("Singapore GP", 6),
                new RaceResult("Azerbaijan GP", 3),
                new RaceResult("Italy GP", 2),
                new RaceResult("Netherlands GP", 4)));
        drivers.put(6L, russell);
    }

    public List<Driver> getAllDrivers() {
        return new ArrayList<>(drivers.values());
    }

    public Driver getDriverById(Long id) {
        return drivers.get(id);
    }
}
