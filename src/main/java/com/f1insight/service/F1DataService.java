package com.f1insight.service;

import com.f1insight.dto.jolpica.JolpicaResponse;
import com.f1insight.model.Circuit;
import com.f1insight.model.Driver;
import com.f1insight.model.Race;
import com.f1insight.model.RaceResult;
import com.f1insight.model.Team;
import com.f1insight.repository.CircuitRepository;
import com.f1insight.repository.DriverRepository;
import com.f1insight.repository.RaceRepository;
import com.f1insight.repository.TeamRepository;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class F1DataService {

    private static final Logger logger = LoggerFactory.getLogger(F1DataService.class);
    private static final String API_URL = "https://api.jolpi.ca/ergast/f1";

    private final DriverRepository driverRepository;
    private final TeamRepository teamRepository;
    private final CircuitRepository circuitRepository;
    private final RaceRepository raceRepository;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    private static final Map<String, String> DRIVER_IMAGES = new HashMap<>();
    private static final Map<String, String> COUNTRY_FLAGS = new HashMap<>();
    private static final Map<String, TeamMetadata> TEAM_METADATA = new HashMap<>();
    private static final Map<String, CircuitMetadata> CIRCUIT_METADATA = new HashMap<>();

    // Takım metadata'sı için yardımcı sınıf
    private static class TeamMetadata {
        String principal;
        String headquarters;
        String countryFlag;
        String teamColor;
        int firstEntry;
        String logoUrl;

        TeamMetadata(String principal, String headquarters, String countryFlag, String teamColor, int firstEntry) {
            this.principal = principal;
            this.headquarters = headquarters;
            this.countryFlag = countryFlag;
            this.teamColor = teamColor;
            this.firstEntry = firstEntry;
            this.logoUrl = null;
        }
    }

    // Pist metadata'sı için yardımcı sınıf
    private static class CircuitMetadata {
        double lengthKm;
        int laps;
        String lapRecordTime;
        String lapRecordHolder;
        int lapRecordYear;
        String countryFlag;
        String imageUrl;

        CircuitMetadata(double lengthKm, int laps, String lapRecordTime, String lapRecordHolder, int lapRecordYear,
                String countryFlag, String imageUrl) {
            this.lengthKm = lengthKm;
            this.laps = laps;
            this.lapRecordTime = lapRecordTime;
            this.lapRecordHolder = lapRecordHolder;
            this.lapRecordYear = lapRecordYear;
            this.countryFlag = countryFlag;
            this.imageUrl = imageUrl;
        }
    }

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
        COUNTRY_FLAGS.put("Swiss", "🇨🇭");

        // 2024 Takım Metadata'ları
        TEAM_METADATA.put("Red Bull", new TeamMetadata(
                "Christian Horner", "Milton Keynes", "🇬🇧", "#3671C6", 2005));
        TEAM_METADATA.put("Ferrari", new TeamMetadata(
                "Frédéric Vasseur", "Maranello", "🇮🇹", "#F91536", 1950));
        TEAM_METADATA.put("Mercedes", new TeamMetadata(
                "Toto Wolff", "Brackley", "🇬🇧", "#6CD3BF", 2010));
        TEAM_METADATA.put("McLaren", new TeamMetadata(
                "Andrea Stella", "Woking", "🇬🇧", "#F58020", 1966));
        TEAM_METADATA.put("Aston Martin", new TeamMetadata(
                "Mike Krack", "Silverstone", "🇬🇧", "#358C75", 2021));
        TEAM_METADATA.put("Alpine F1 Team", new TeamMetadata(
                "Bruno Famin", "Enstone", "🇫🇷", "#2293D1", 2021));
        TEAM_METADATA.put("Williams", new TeamMetadata(
                "James Vowles", "Grove", "🇬🇧", "#37BEDD", 1977));
        TEAM_METADATA.put("RB F1 Team", new TeamMetadata(
                "Laurent Mekies", "Faenza", "🇮🇹", "#6692FF", 2024));
        TEAM_METADATA.put("Kick Sauber", new TeamMetadata(
                "Alessandro Alunni Bravi", "Hinwil", "🇨🇭", "#52E252", 1993));
        TEAM_METADATA.put("Haas F1 Team", new TeamMetadata(
                "Ayao Komatsu", "Kannapolis", "🇺🇸", "#B6BABD", 2016));

        // 2024 Pist Metadata'ları (with circuit layout images from F1 media)
        CIRCUIT_METADATA.put("bahrain", new CircuitMetadata(5.412, 57, "1:31.447", "P. de la Rosa", 2005, "🇧🇭",
                "https://media.formula1.com/image/upload/f_auto/q_auto/v1677245010/content/dam/fom-website/2018-redesign-assets/Circuit%20Layouts%202024/Bahrain.png"));
        CIRCUIT_METADATA.put("jeddah", new CircuitMetadata(6.174, 50, "1:30.734", "L. Hamilton", 2021, "🇸🇦",
                "https://media.formula1.com/image/upload/f_auto/q_auto/v1677245010/content/dam/fom-website/2018-redesign-assets/Circuit%20Layouts%202024/Saudi%20Arabia.png"));
        CIRCUIT_METADATA.put("albert_park", new CircuitMetadata(5.278, 58, "1:20.235", "C. Leclerc", 2024, "🇦🇺",
                "https://media.formula1.com/image/upload/f_auto/q_auto/v1677245010/content/dam/fom-website/2018-redesign-assets/Circuit%20Layouts%202024/Australia.png"));
        CIRCUIT_METADATA.put("suzuka", new CircuitMetadata(5.807, 53, "1:30.983", "L. Hamilton", 2019, "🇯🇵",
                "https://media.formula1.com/image/upload/f_auto/q_auto/v1677245010/content/dam/fom-website/2018-redesign-assets/Circuit%20Layouts%202024/Japan.png"));
        CIRCUIT_METADATA.put("shanghai", new CircuitMetadata(5.451, 56, "1:32.238", "M. Schumacher", 2004, "🇨🇳",
                "https://media.formula1.com/image/upload/f_auto/q_auto/v1677245010/content/dam/fom-website/2018-redesign-assets/Circuit%20Layouts%202024/China.png"));
        CIRCUIT_METADATA.put("miami", new CircuitMetadata(5.412, 57, "1:29.708", "M. Verstappen", 2023, "🇺🇸",
                "https://media.formula1.com/image/upload/f_auto/q_auto/v1677245010/content/dam/fom-website/2018-redesign-assets/Circuit%20Layouts%202024/Miami.png"));
        CIRCUIT_METADATA.put("imola", new CircuitMetadata(4.909, 63, "1:15.484", "L. Hamilton", 2020, "🇮🇹",
                "https://media.formula1.com/image/upload/f_auto/q_auto/v1677245010/content/dam/fom-website/2018-redesign-assets/Circuit%20Layouts%202024/Emilia%20Romagna.png"));
        CIRCUIT_METADATA.put("monaco", new CircuitMetadata(3.337, 78, "1:12.909", "L. Hamilton", 2021, "🇲🇨",
                "https://media.formula1.com/image/upload/f_auto/q_auto/v1677245010/content/dam/fom-website/2018-redesign-assets/Circuit%20Layouts%202024/Monaco.png"));
        CIRCUIT_METADATA.put("villeneuve", new CircuitMetadata(4.361, 70, "1:13.078", "V. Bottas", 2019, "🇨🇦",
                "https://media.formula1.com/image/upload/f_auto/q_auto/v1677245010/content/dam/fom-website/2018-redesign-assets/Circuit%20Layouts%202024/Canada.png"));
        CIRCUIT_METADATA.put("catalunya", new CircuitMetadata(4.657, 66, "1:16.330", "M. Verstappen", 2023, "🇪🇸",
                "https://media.formula1.com/image/upload/f_auto/q_auto/v1677245010/content/dam/fom-website/2018-redesign-assets/Circuit%20Layouts%202024/Spain.png"));
        CIRCUIT_METADATA.put("red_bull_ring", new CircuitMetadata(4.318, 71, "1:05.619", "C. Sainz", 2020, "🇦🇹",
                "https://media.formula1.com/image/upload/f_auto/q_auto/v1677245010/content/dam/fom-website/2018-redesign-assets/Circuit%20Layouts%202024/Austria.png"));
        CIRCUIT_METADATA.put("silverstone", new CircuitMetadata(5.891, 52, "1:27.097", "M. Verstappen", 2020, "🇬🇧",
                "https://media.formula1.com/image/upload/f_auto/q_auto/v1677245010/content/dam/fom-website/2018-redesign-assets/Circuit%20Layouts%202024/Great%20Britain.png"));
        CIRCUIT_METADATA.put("hungaroring", new CircuitMetadata(4.381, 70, "1:16.627", "L. Hamilton", 2020, "🇭🇺",
                "https://media.formula1.com/image/upload/f_auto/q_auto/v1677245010/content/dam/fom-website/2018-redesign-assets/Circuit%20Layouts%202024/Hungary.png"));
        CIRCUIT_METADATA.put("spa", new CircuitMetadata(7.004, 44, "1:46.286", "V. Bottas", 2018, "🇧🇪",
                "https://media.formula1.com/image/upload/f_auto/q_auto/v1677245010/content/dam/fom-website/2018-redesign-assets/Circuit%20Layouts%202024/Belgium.png"));
        CIRCUIT_METADATA.put("zandvoort", new CircuitMetadata(4.259, 72, "1:11.097", "L. Hamilton", 2021, "🇳🇱",
                "https://media.formula1.com/image/upload/f_auto/q_auto/v1677245010/content/dam/fom-website/2018-redesign-assets/Circuit%20Layouts%202024/Netherlands.png"));
        CIRCUIT_METADATA.put("monza", new CircuitMetadata(5.793, 53, "1:21.046", "R. Barrichello", 2004, "🇮🇹",
                "https://media.formula1.com/image/upload/f_auto/q_auto/v1677245010/content/dam/fom-website/2018-redesign-assets/Circuit%20Layouts%202024/Italy.png"));
        CIRCUIT_METADATA.put("baku", new CircuitMetadata(6.003, 51, "1:43.009", "C. Leclerc", 2019, "🇦🇿",
                "https://media.formula1.com/image/upload/f_auto/q_auto/v1677245010/content/dam/fom-website/2018-redesign-assets/Circuit%20Layouts%202024/Azerbaijan.png"));
        CIRCUIT_METADATA.put("marina_bay", new CircuitMetadata(4.940, 62, "1:35.867", "L. Hamilton", 2023, "🇸🇬",
                "https://media.formula1.com/image/upload/f_auto/q_auto/v1677245010/content/dam/fom-website/2018-redesign-assets/Circuit%20Layouts%202024/Singapore.png"));
        CIRCUIT_METADATA.put("americas", new CircuitMetadata(5.513, 56, "1:36.169", "C. Leclerc", 2019, "🇺🇸",
                "https://media.formula1.com/image/upload/f_auto/q_auto/v1677245010/content/dam/fom-website/2018-redesign-assets/Circuit%20Layouts%202024/USA.png"));
        CIRCUIT_METADATA.put("rodriguez", new CircuitMetadata(4.304, 71, "1:17.774", "V. Bottas", 2021, "🇲🇽",
                "https://media.formula1.com/image/upload/f_auto/q_auto/v1677245010/content/dam/fom-website/2018-redesign-assets/Circuit%20Layouts%202024/Mexico.png"));
        CIRCUIT_METADATA.put("interlagos", new CircuitMetadata(4.309, 71, "1:10.540", "V. Bottas", 2018, "🇧🇷",
                "https://media.formula1.com/image/upload/f_auto/q_auto/v1677245010/content/dam/fom-website/2018-redesign-assets/Circuit%20Layouts%202024/Brazil.png"));
        CIRCUIT_METADATA.put("vegas", new CircuitMetadata(6.201, 50, "1:35.490", "O. Piastri", 2023, "🇺🇸",
                "https://media.formula1.com/image/upload/f_auto/q_auto/v1677245010/content/dam/fom-website/2018-redesign-assets/Circuit%20Layouts%202024/Las%20Vegas.png"));
        CIRCUIT_METADATA.put("losail", new CircuitMetadata(5.380, 57, "1:24.319", "M. Verstappen", 2023, "🇶🇦",
                "https://media.formula1.com/image/upload/f_auto/q_auto/v1677245010/content/dam/fom-website/2018-redesign-assets/Circuit%20Layouts%202024/Qatar.png"));
        CIRCUIT_METADATA.put("yas_marina", new CircuitMetadata(5.281, 58, "1:26.103", "M. Verstappen", 2021, "🇦🇪",
                "https://media.formula1.com/image/upload/f_auto/q_auto/v1677245010/content/dam/fom-website/2018-redesign-assets/Circuit%20Layouts%202024/Abu%20Dhabi.png"));
    }

    public F1DataService(DriverRepository driverRepository, TeamRepository teamRepository,
            CircuitRepository circuitRepository, RaceRepository raceRepository,
            ObjectMapper objectMapper) {
        this.driverRepository = driverRepository;
        this.teamRepository = teamRepository;
        this.circuitRepository = circuitRepository;
        this.raceRepository = raceRepository;
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
     * Yarış takvimini ve sonuçlarını çek ve kaydet
     */
    @Transactional
    public void fetchAndSaveRaceCalendar(int year) {
        logger.info("{} yılı için yarış takvimi çekiliyor...", year);

        try {
            // Yarış takvimini çek
            String url = API_URL + "/" + year + ".json?limit=30";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .header("User-Agent", "F1InsightDeck/1.0")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                RaceCalendarResponse calendarResponse = objectMapper.readValue(response.body(),
                        RaceCalendarResponse.class);

                if (calendarResponse != null && calendarResponse.mrData != null &&
                        calendarResponse.mrData.raceTable != null &&
                        calendarResponse.mrData.raceTable.races != null) {

                    for (RaceDto raceDto : calendarResponse.mrData.raceTable.races) {
                        saveRace(year, raceDto);
                    }

                    logger.info("{} yarış takvimi başarıyla kaydedildi.",
                            calendarResponse.mrData.raceTable.races.size());

                    // Her yarışın sonucunu çek
                    fetchAndSaveRaceResults(year);
                }
            } else {
                logger.error("Yarış takvimi API hatası: {}", response.statusCode());
            }

        } catch (Exception e) {
            logger.error("Yarış takvimi çekme sırasında hata oluştu: ", e);
        }
    }

    /**
     * Her yarışın sonucunu çek
     */
    private void fetchAndSaveRaceResults(int year) {
        List<Race> races = raceRepository.findBySeasonOrderByRoundAsc(year);

        for (Race race : races) {
            try {
                Thread.sleep(100); // API rate limit için bekle
                fetchSingleRaceResult(year, race.getRound(), race);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Tek bir yarışın sonucunu çek
     */
    private void fetchSingleRaceResult(int year, int round, Race race) {
        try {
            String url = API_URL + "/" + year + "/" + round + "/results.json?limit=1";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .header("User-Agent", "F1InsightDeck/1.0")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                NewRaceResultsResponse resultsResponse = objectMapper.readValue(response.body(),
                        NewRaceResultsResponse.class);

                if (resultsResponse != null && resultsResponse.mrData != null &&
                        resultsResponse.mrData.raceTable != null &&
                        resultsResponse.mrData.raceTable.races != null &&
                        !resultsResponse.mrData.raceTable.races.isEmpty()) {

                    NewRaceResultDto raceResult = resultsResponse.mrData.raceTable.races.get(0);
                    if (raceResult.results != null && !raceResult.results.isEmpty()) {
                        NewResultDto winner = raceResult.results.get(0);

                        String winnerName = winner.driver.givenName + " " + winner.driver.familyName;
                        race.setWinnerName(winnerName);
                        race.setWinnerTeam(winner.constructor.name);
                        race.setWinnerImageUrl(DRIVER_IMAGES.get(winnerName));

                        // En hızlı tur bilgisi
                        if (winner.fastestLap != null && winner.fastestLap.time != null) {
                            race.setFastestLapTime(winner.fastestLap.time.time);
                            race.setFastestLapDriver(winnerName);
                        }

                        raceRepository.save(race);
                        logger.debug("Yarış {} sonucu kaydedildi: {}", round, winnerName);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Yarış {} sonucu çekilemedi: {}", round, e.getMessage());
        }
    }

    /**
     * Circuit entity'si oluştur veya getir
     */
    private Circuit getOrCreateCircuit(CircuitDto circuitDto) {
        return circuitRepository.findByCircuitId(circuitDto.circuitId)
                .orElseGet(() -> {
                    Circuit circuit = new Circuit(circuitDto.circuitId, circuitDto.circuitName);
                    circuit.setCountry(circuitDto.location.country);
                    circuit.setLocality(circuitDto.location.locality);
                    populateCircuitMetadata(circuit);
                    return circuitRepository.save(circuit);
                });
    }

    /**
     * Pist metadata'sını statik mapping'den doldur
     */
    private void populateCircuitMetadata(Circuit circuit) {
        CircuitMetadata metadata = CIRCUIT_METADATA.get(circuit.getCircuitId());

        if (metadata != null) {
            circuit.setLengthKm(metadata.lengthKm);
            circuit.setLaps(metadata.laps);
            circuit.setLapRecordTime(metadata.lapRecordTime);
            circuit.setLapRecordHolder(metadata.lapRecordHolder);
            circuit.setLapRecordYear(metadata.lapRecordYear);
            circuit.setCountryFlag(metadata.countryFlag);
            circuit.setImageUrl(metadata.imageUrl);
        } else {
            circuit.setCountryFlag("🏁");
            logger.warn("Pist metadata'sı bulunamadı: {}", circuit.getCircuitId());
        }
    }

    /**
     * Yarışı kaydet
     */
    private void saveRace(int year, RaceDto raceDto) {
        Circuit circuit = getOrCreateCircuit(raceDto.circuit);

        Race race = raceRepository.findBySeasonAndRound(year, Integer.parseInt(raceDto.round))
                .orElse(new Race(year, Integer.parseInt(raceDto.round), raceDto.raceName));

        race.setRaceName(raceDto.raceName);
        race.setRaceDate(LocalDate.parse(raceDto.date));
        race.setCircuit(circuit);

        raceRepository.save(race);
    }

    // ===== Race Calendar DTO'ları =====
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class RaceCalendarResponse {
        @JsonProperty("MRData")
        MRDataCalendar mrData;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class MRDataCalendar {
        @JsonProperty("RaceTable")
        RaceTableDto raceTable;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class RaceTableDto {
        @JsonProperty("Races")
        List<RaceDto> races;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class RaceDto {
        @JsonProperty("round")
        String round;
        @JsonProperty("raceName")
        String raceName;
        @JsonProperty("date")
        String date;
        @JsonProperty("Circuit")
        CircuitDto circuit;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class CircuitDto {
        @JsonProperty("circuitId")
        String circuitId;
        @JsonProperty("circuitName")
        String circuitName;
        @JsonProperty("Location")
        LocationDto location;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class LocationDto {
        @JsonProperty("locality")
        String locality;
        @JsonProperty("country")
        String country;
    }

    // ===== Race Results DTO'ları (fetchSingleRaceResult için) =====
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class NewRaceResultsResponse {
        @JsonProperty("MRData")
        NewMRDataResults mrData;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class NewMRDataResults {
        @JsonProperty("RaceTable")
        NewRaceTableResultsDto raceTable;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class NewRaceTableResultsDto {
        @JsonProperty("Races")
        List<NewRaceResultDto> races;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class NewRaceResultDto {
        @JsonProperty("Results")
        List<NewResultDto> results;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class NewResultDto {
        @JsonProperty("Driver")
        NewDriverResultDto driver;
        @JsonProperty("Constructor")
        NewConstructorResultDto constructor;
        @JsonProperty("FastestLap")
        NewFastestLapDto fastestLap;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class NewDriverResultDto {
        @JsonProperty("givenName")
        String givenName;
        @JsonProperty("familyName")
        String familyName;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class NewConstructorResultDto {
        @JsonProperty("name")
        String name;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class NewFastestLapDto {
        @JsonProperty("Time")
        NewLapTimeDto time;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class NewLapTimeDto {
        @JsonProperty("time")
        String time;
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
        String teamNationality = constructorDto != null ? constructorDto.getNationality() : "Unknown";

        // Takımı oluştur veya getir
        Team team = getOrCreateTeam(teamName, teamNationality);

        // Mevcut driver'ı kontrol et veya yeni oluştur
        Driver driver = driverRepository.findByName(fullName)
                .orElse(new Driver());

        driver.setName(fullName);
        driver.setTeam(teamName);
        driver.setTeamEntity(team); // Team entity ilişkisini ayarla
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

    /**
     * Takım entity'si oluştur veya mevcut olanı getir
     * Statik metadata ile zenginleştir
     */
    private Team getOrCreateTeam(String teamName, String nationality) {
        return teamRepository.findByName(teamName)
                .orElseGet(() -> {
                    Team team = new Team(teamName);
                    team.setNationality(nationality);
                    populateTeamMetadata(team);
                    return teamRepository.save(team);
                });
    }

    /**
     * Takım metadata'sını statik mapping'den doldur
     */
    private void populateTeamMetadata(Team team) {
        TeamMetadata metadata = TEAM_METADATA.get(team.getName());

        if (metadata != null) {
            team.setTeamPrincipal(metadata.principal);
            team.setHeadquarters(metadata.headquarters);
            team.setCountryFlag(metadata.countryFlag);
            team.setTeamColor(metadata.teamColor);
            team.setFirstEntry(metadata.firstEntry);
            team.setLogoUrl(metadata.logoUrl);
        } else {
            // Fallback değerler
            team.setTeamColor("#CCCCCC");
            team.setCountryFlag("🏁");
            logger.warn("Takım metadata'sı bulunamadı: {}", team.getName());
        }
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
