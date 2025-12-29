package com.f1insight.service;

import com.f1insight.dto.jolpica.JolpicaResponse;
import com.f1insight.model.Driver;
import com.f1insight.model.RaceResult;
import com.f1insight.repository.DriverRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class F1DataService {

    private static final Logger logger = LoggerFactory.getLogger(F1DataService.class);
    private static final String API_URL = "https://api.jolpi.ca/ergast/f1";

    private final DriverRepository driverRepository;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    // Pilot fotoğrafları için mapping (F1 resmi sitesi URL'leri)
    private static final Map<String, String> DRIVER_IMAGES = new HashMap<>();
    private static final Map<String, String> COUNTRY_FLAGS = new HashMap<>();

    static {
        // 2024 Pilot Fotoğrafları (F1 Resmi)
        DRIVER_IMAGES.put("Max Verstappen",
                "https://media.formula1.com/content/dam/fom-website/drivers/2024Drivers/verstappen.jpg.img.1920.medium.jpg/1708344545893.jpg");
        DRIVER_IMAGES.put("Lando Norris",
                "https://media.formula1.com/content/dam/fom-website/drivers/2024Drivers/norris.jpg.img.1920.medium.jpg/1708344470980.jpg");
        DRIVER_IMAGES.put("Oscar Piastri",
                "https://media.formula1.com/content/dam/fom-website/drivers/2024Drivers/piastri.jpg.img.1920.medium.jpg/1708344470980.jpg");
        DRIVER_IMAGES.put("Charles Leclerc",
                "https://media.formula1.com/content/dam/fom-website/drivers/2024Drivers/leclerc.jpg.img.1920.medium.jpg/1708344363428.jpg");
        DRIVER_IMAGES.put("Lewis Hamilton",
                "https://media.formula1.com/content/dam/fom-website/drivers/2024Drivers/hamilton.jpg.img.1920.medium.jpg/1708344465302.jpg");
        DRIVER_IMAGES.put("George Russell",
                "https://media.formula1.com/content/dam/fom-website/drivers/2024Drivers/russell.jpg.img.1920.medium.jpg/1708344472658.jpg");
        DRIVER_IMAGES.put("Carlos Sainz",
                "https://media.formula1.com/content/dam/fom-website/drivers/2024Drivers/sainz.jpg.img.1920.medium.jpg/1708344449073.jpg");
        DRIVER_IMAGES.put("Sergio Pérez",
                "https://media.formula1.com/content/dam/fom-website/drivers/2024Drivers/perez.jpg.img.1920.medium.jpg/1708344489679.jpg");
        DRIVER_IMAGES.put("Fernando Alonso",
                "https://media.formula1.com/content/dam/fom-website/drivers/2024Drivers/alonso.jpg.img.1920.medium.jpg/1708344424139.jpg");
        DRIVER_IMAGES.put("Pierre Gasly",
                "https://media.formula1.com/content/dam/fom-website/drivers/2024Drivers/gasly.jpg.img.1920.medium.jpg/1708344376270.jpg");
        DRIVER_IMAGES.put("Esteban Ocon",
                "https://media.formula1.com/content/dam/fom-website/drivers/2024Drivers/ocon.jpg.img.1920.medium.jpg/1708344455564.jpg");
        DRIVER_IMAGES.put("Lance Stroll",
                "https://media.formula1.com/content/dam/fom-website/drivers/2024Drivers/stroll.jpg.img.1920.medium.jpg/1708344438987.jpg");
        DRIVER_IMAGES.put("Yuki Tsunoda",
                "https://media.formula1.com/content/dam/fom-website/drivers/2024Drivers/tsunoda.jpg.img.1920.medium.jpg/1708344549922.jpg");
        DRIVER_IMAGES.put("Alexander Albon",
                "https://media.formula1.com/content/dam/fom-website/drivers/2024Drivers/albon.jpg.img.1920.medium.jpg/1708344528688.jpg");
        DRIVER_IMAGES.put("Nico Hülkenberg",
                "https://media.formula1.com/content/dam/fom-website/drivers/2024Drivers/hulkenberg.jpg.img.1920.medium.jpg/1708344477195.jpg");
        DRIVER_IMAGES.put("Kevin Magnussen",
                "https://media.formula1.com/content/dam/fom-website/drivers/2024Drivers/magnussen.jpg.img.1920.medium.jpg/1708344433032.jpg");
        DRIVER_IMAGES.put("Daniel Ricciardo",
                "https://media.formula1.com/content/dam/fom-website/drivers/2024Drivers/ricciardo.jpg.img.1920.medium.jpg/1708344466894.jpg");
        DRIVER_IMAGES.put("Oliver Bearman",
                "https://media.formula1.com/content/dam/fom-website/drivers/2024Drivers/bearman.jpg.img.1920.medium.jpg/1708344483158.jpg");
        DRIVER_IMAGES.put("Liam Lawson",
                "https://media.formula1.com/content/dam/fom-website/drivers/2024Drivers/lawson.jpg.img.1920.medium.jpg/1708344487167.jpg");
        DRIVER_IMAGES.put("Franco Colapinto",
                "https://media.formula1.com/content/dam/fom-website/drivers/2024Drivers/colapinto.jpg.img.1920.medium.jpg/1724328939498.jpg");
        DRIVER_IMAGES.put("Guanyu Zhou",
                "https://media.formula1.com/content/dam/fom-website/drivers/2024Drivers/zhou.jpg.img.1920.medium.jpg/1708344554330.jpg");
        DRIVER_IMAGES.put("Valtteri Bottas",
                "https://media.formula1.com/content/dam/fom-website/drivers/2024Drivers/bottas.jpg.img.1920.medium.jpg/1708344485842.jpg");
        DRIVER_IMAGES.put("Logan Sargeant",
                "https://media.formula1.com/content/dam/fom-website/drivers/2024Drivers/sargeant.jpg.img.1920.medium.jpg/1708344447531.jpg");
        DRIVER_IMAGES.put("Jack Doohan",
                "https://media.formula1.com/content/dam/fom-website/drivers/2024Drivers/doohan.jpg.img.1920.medium.jpg/1708344493171.jpg");

        // Ülke bayrakları (Emoji)
        COUNTRY_FLAGS.put("Dutch", "🇳🇱");
        COUNTRY_FLAGS.put("British", "🇬🇧");
        COUNTRY_FLAGS.put("German", "🇩🇪");
        COUNTRY_FLAGS.put("Spanish", "🇪🇸");
        COUNTRY_FLAGS.put("Monegasque", "🇲🇨");
        COUNTRY_FLAGS.put("French", "🇫🇷");
        COUNTRY_FLAGS.put("Australian", "🇦🇺");
        COUNTRY_FLAGS.put("Mexican", "🇲🇽");
        COUNTRY_FLAGS.put("Finnish", "🇫🇮");
        COUNTRY_FLAGS.put("American", "🇺🇸");
        COUNTRY_FLAGS.put("Thai", "🇹🇭");
        COUNTRY_FLAGS.put("Canadian", "🇨🇦");
        COUNTRY_FLAGS.put("Japanese", "🇯🇵");
        COUNTRY_FLAGS.put("Chinese", "🇨🇳");
        COUNTRY_FLAGS.put("Danish", "🇩🇰");
        COUNTRY_FLAGS.put("Italian", "🇮🇹");
        COUNTRY_FLAGS.put("Brazilian", "🇧🇷");
        COUNTRY_FLAGS.put("New Zealander", "🇳🇿");
        COUNTRY_FLAGS.put("Argentine", "🇦🇷");
    }

    public F1DataService(DriverRepository driverRepository, ObjectMapper objectMapper) {
        this.driverRepository = driverRepository;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newHttpClient();
    }

    @Transactional
    public void fetchAndSaveDriverStandings(int year) {
        logger.info("{} yılı için pilot verileri çekiliyor...", year);

        try {
            // Yarış sonuçlarını çek (podium + son 10 yarış)
            RaceData raceData = fetchRaceData(year);

            String url = API_URL + "/" + year + "/driverStandings.json?limit=100";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .header("User-Agent", "F1InsightDeck/1.0")
                    .GET()
                    .build();

            HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            logger.info("API yanıt kodu: {}", httpResponse.statusCode());

            if (httpResponse.statusCode() == 200) {
                String jsonBody = httpResponse.body();
                JolpicaResponse response = objectMapper.readValue(jsonBody, JolpicaResponse.class);

                if (response != null && response.getMrData() != null &&
                        response.getMrData().getStandingsTable() != null &&
                        response.getMrData().getStandingsTable().getStandingsLists() != null &&
                        !response.getMrData().getStandingsTable().getStandingsLists().isEmpty()) {

                    var driverStandings = response.getMrData().getStandingsTable().getStandingsLists().get(0)
                            .getDriverStandings();

                    for (var standing : driverStandings) {
                        saveDriver(standing, raceData);
                    }

                    logger.info("{} pilot başarıyla veritabanına kaydedildi.", driverStandings.size());
                } else {
                    logger.warn("{} yılı için veri bulunamadı!", year);
                }
            } else {
                logger.error("API hatası: {} - {}", httpResponse.statusCode(),
                        httpResponse.body().substring(0, Math.min(200, httpResponse.body().length())));
            }

        } catch (Exception e) {
            logger.error("Veri çekme sırasında hata oluştu: ", e);
        }
    }

    /**
     * Yarış verilerini tutan yardımcı sınıf
     */
    private static class RaceData {
        Map<String, Integer> podiumCounts = new HashMap<>();
        Map<String, List<RaceInfo>> driverRaces = new HashMap<>();
    }

    private static class RaceInfo {
        String raceName;
        int position;

        RaceInfo(String raceName, int position) {
            this.raceName = raceName;
            this.position = position;
        }
    }

    /**
     * Yarış sonuçlarından podium sayılarını ve yarış sonuçlarını çeker
     * API max 100 sonuç döndürdüğü için pagination kullanıyoruz
     */
    private RaceData fetchRaceData(int year) {
        RaceData raceData = new RaceData();

        try {
            int offset = 0;
            int limit = 100;
            int total = 0;

            do {
                String url = API_URL + "/" + year + "/results.json?limit=" + limit + "&offset=" + offset;
                logger.info("Yarış sonuçları çekiliyor: offset={}", offset);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Accept", "application/json")
                        .header("User-Agent", "F1InsightDeck/1.0")
                        .GET()
                        .build();

                HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (httpResponse.statusCode() == 200) {
                    RaceResultsResponse response = objectMapper.readValue(httpResponse.body(),
                            RaceResultsResponse.class);

                    if (response != null && response.mrData != null) {
                        // İlk istekte total değeri al
                        if (offset == 0) {
                            total = Integer.parseInt(response.mrData.total);
                            logger.info("Toplam yarış sonucu: {}", total);
                        }

                        if (response.mrData.raceTable != null && response.mrData.raceTable.races != null) {
                            for (RaceResultsResponse.Race race : response.mrData.raceTable.races) {
                                if (race.results != null) {
                                    for (RaceResultsResponse.Result result : race.results) {
                                        try {
                                            int position = Integer.parseInt(result.position);
                                            String driverName = result.driver.givenName + " "
                                                    + result.driver.familyName;

                                            // Podium sayısı
                                            if (position >= 1 && position <= 3) {
                                                raceData.podiumCounts.merge(driverName, 1, Integer::sum);
                                            }

                                            // Yarış sonucunu kaydet
                                            raceData.driverRaces
                                                    .computeIfAbsent(driverName, k -> new ArrayList<>())
                                                    .add(new RaceInfo(race.raceName, position));

                                        } catch (NumberFormatException e) {
                                            // "R" (Retired) gibi değerler - yarış adını yine de kaydet
                                            String driverName = result.driver.givenName + " "
                                                    + result.driver.familyName;
                                            raceData.driverRaces
                                                    .computeIfAbsent(driverName, k -> new ArrayList<>())
                                                    .add(new RaceInfo(race.raceName, 20)); // DNF için 20
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                offset += limit;

                // Rate limiting - API'yi yormamak için kısa bekleme
                Thread.sleep(100);

            } while (offset < total);

            // Debug log
            logger.info("Podium sayıları hesaplandı: {} pilot", raceData.podiumCounts.size());
            logger.info("Yarış sonuçları toplandı: {} pilot", raceData.driverRaces.size());

        } catch (Exception e) {
            logger.warn("Yarış verileri çekilemedi: {}", e.getMessage());
        }

        return raceData;
    }

    private void saveDriver(JolpicaResponse.DriverStanding standing, RaceData raceData) {
        var driverDto = standing.getDriver();
        var constructorDto = standing.getConstructors().isEmpty() ? null : standing.getConstructors().get(0);

        String fullName = driverDto.getGivenName() + " " + driverDto.getFamilyName();
        String teamName = constructorDto != null ? constructorDto.getName() : "Unknown";

        // Mevcut driver'ı kontrol et veya yeni oluştur
        Driver driver = driverRepository.findByName(fullName)
                .orElse(new Driver());

        driver.setName(fullName);
        driver.setTeam(teamName);
        driver.setPoints(parseInteger(standing.getPoints()));
        driver.setWins(parseInteger(standing.getWins()));
        driver.setCountry(driverDto.getNationality());

        // Gerçek podium sayısını kullan
        int podiums = raceData.podiumCounts.getOrDefault(fullName, 0);
        driver.setPodiums(podiums);

        // Görsel verileri ayarla
        populateVisualData(driver);

        // Önce driver'ı kaydet (ID alması için)
        driver = driverRepository.save(driver);

        // Son 10 yarış sonucunu ekle
        List<RaceInfo> allRaces = raceData.driverRaces.getOrDefault(fullName, new ArrayList<>());
        // Son 10 yarışı al (listedeki son 10)
        int startIndex = Math.max(0, allRaces.size() - 10);
        List<RaceInfo> last10Races = allRaces.subList(startIndex, allRaces.size());

        // Mevcut yarış sonuçlarını temizle
        if (driver.getLastTenRaces() != null) {
            driver.getLastTenRaces().clear();
        } else {
            driver.setLastTenRaces(new ArrayList<>());
        }

        // Yeni yarış sonuçlarını ekle
        for (RaceInfo raceInfo : last10Races) {
            RaceResult raceResult = new RaceResult(raceInfo.raceName, raceInfo.position);
            raceResult.setDriver(driver);
            driver.getLastTenRaces().add(raceResult);
        }

        driverRepository.save(driver);
        logger.debug("Pilot kaydedildi: {} - {} yarış sonucu", fullName, last10Races.size());
    }

    private int parseInteger(String value) {
        try {
            return (int) Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void populateVisualData(Driver driver) {
        String team = driver.getTeam().toLowerCase();
        String name = driver.getName();

        // Takım renkleri (Hex)
        if (team.contains("red bull"))
            driver.setTeamColor("#3671C6");
        else if (team.contains("ferrari"))
            driver.setTeamColor("#F91536");
        else if (team.contains("mercedes"))
            driver.setTeamColor("#6CD3BF");
        else if (team.contains("mclaren"))
            driver.setTeamColor("#F58020");
        else if (team.contains("aston martin"))
            driver.setTeamColor("#358C75");
        else if (team.contains("alpine"))
            driver.setTeamColor("#2293D1");
        else if (team.contains("williams"))
            driver.setTeamColor("#37BEDD");
        else if (team.contains("rb") || team.contains("alphatauri"))
            driver.setTeamColor("#6692FF");
        else if (team.contains("kick") || team.contains("sauber") || team.contains("alfa romeo"))
            driver.setTeamColor("#52E252");
        else if (team.contains("haas"))
            driver.setTeamColor("#B6BABD");
        else
            driver.setTeamColor("#CCCCCC");

        // Pilot fotoğrafları - mapping'den al
        String imageUrl = DRIVER_IMAGES.get(name);
        if (imageUrl != null) {
            driver.setImageUrl(imageUrl);
        } else {
            // Fallback: Varsayılan placeholder
            driver.setImageUrl("https://via.placeholder.com/400x400/1a1a1a/ffffff?text=" + name.replace(" ", "+"));
        }

        // Ülke bayrakları - emoji kullan
        String flag = COUNTRY_FLAGS.get(driver.getCountry());
        if (flag != null) {
            driver.setCountryFlag(flag);
        } else {
            driver.setCountryFlag("🏁");
        }

        // Takım logoları
        driver.setTeamLogo("/images/teams/" + team.replace(" ", "_") + ".png");
    }

    // DTO sınıfları - Yarış sonuçları için
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class RaceResultsResponse {
        @JsonProperty("MRData")
        public MRData mrData;

        @JsonIgnoreProperties(ignoreUnknown = true)
        static class MRData {
            @JsonProperty("total")
            public String total;
            @JsonProperty("RaceTable")
            public RaceTable raceTable;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        static class RaceTable {
            @JsonProperty("Races")
            public List<Race> races;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        static class Race {
            @JsonProperty("raceName")
            public String raceName;
            @JsonProperty("Results")
            public List<Result> results;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        static class Result {
            @JsonProperty("position")
            public String position;
            @JsonProperty("Driver")
            public DriverInfo driver;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        static class DriverInfo {
            @JsonProperty("givenName")
            public String givenName;
            @JsonProperty("familyName")
            public String familyName;
        }
    }
}
