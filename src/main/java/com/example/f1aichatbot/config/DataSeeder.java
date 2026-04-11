package com.example.f1aichatbot.config;

import com.example.f1aichatbot.model.Driver;
import com.example.f1aichatbot.model.Race;
import com.example.f1aichatbot.model.Team;
import com.example.f1aichatbot.repository.DriverRepository;
import com.example.f1aichatbot.repository.RaceRepository;
import com.example.f1aichatbot.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final DriverRepository driverRepository;
    private final TeamRepository teamRepository;
    private final RaceRepository raceRepository;


    @Override
    public void run(String... args) {
        if (driverRepository.count() == 0){
            seedTeams();
            seedDrivers();
            seedRaces2025();
            log.info("F1 AI Chatbot Data Seeded");
        }
    }

    private void seedTeams() {
        List<Team> teams = List.of(
                Team.builder().name("Red Bull Racing").fullName("Oracle Red Bull Racing")
                        .nationality("Austrian").baseLocation("Milton Keynes, UK")
                        .constructorChampionships(6).raceWins(120).foundedYear(2005)
                        .teamPrincipal("Laurent Mekies").powerUnit("Honda RBPT").isActive(true)
                        .description("The dominant team of the modern era, powered by Max Verstappen's championship-winning performances.")
                        .build(),

                Team.builder().name("Ferrari").fullName("Scuderia Ferrari HP")
                        .nationality("Italian").baseLocation("Maranello, Italy")
                        .constructorChampionships(16).raceWins(243).foundedYear(1950)
                        .teamPrincipal("Frederic Vasseur").powerUnit("Ferrari").isActive(true)
                        .description("The most historic team in Formula 1 with 16 Constructors' Championships.")
                        .build(),

                Team.builder().name("Mercedes").fullName("Mercedes-AMG PETRONAS F1 Team")
                        .nationality("German").baseLocation("Brackley, UK")
                        .constructorChampionships(8).raceWins(125).foundedYear(1954)
                        .teamPrincipal("Toto Wolff").powerUnit("Mercedes").isActive(true)
                        .description("A legendary team that dominated the hybrid era with 8 consecutive titles (2014–2021).")
                        .build(),

                Team.builder().name("McLaren").fullName("McLaren Formula 1 Team")
                        .nationality("British").baseLocation("Woking, UK")
                        .constructorChampionships(8).raceWins(183).foundedYear(1966)
                        .teamPrincipal("Andrea Stella").powerUnit("Mercedes").isActive(true)
                        .description("A resurging top team and 2024 Constructors' Championship contender.")
                        .build(),

                Team.builder().name("Aston Martin").fullName("Aston Martin Aramco F1 Team")
                        .nationality("British").baseLocation("Silverstone, UK")
                        .constructorChampionships(0).raceWins(0).foundedYear(2018)
                        .teamPrincipal("Mike Krack").powerUnit("Mercedes").isActive(true)
                        .description("Home to experienced driver Fernando Alonso and Lance Stroll, aiming for consistent podiums.")
                        .build(),

                Team.builder().name("Alpine").fullName("BWT Alpine F1 Team")
                        .nationality("French").baseLocation("Enstone, UK")
                        .constructorChampionships(2).raceWins(21).foundedYear(1986)
                        .teamPrincipal("Oliver Oakes").powerUnit("Renault").isActive(true)
                        .description("The French works team continuing Renault's legacy in Formula 1.")
                        .build(),

                Team.builder().name("Williams").fullName("Williams Racing")
                        .nationality("British").baseLocation("Grove, UK")
                        .constructorChampionships(7).raceWins(114).foundedYear(1977)
                        .teamPrincipal("James Vowles").powerUnit("Mercedes").isActive(true)
                        .description("One of the most successful independent teams in F1 history with 7 titles.")
                        .build(),

                Team.builder().name("Haas").fullName("MoneyGram Haas F1 Team")
                        .nationality("American").baseLocation("Kannapolis, USA")
                        .constructorChampionships(0).raceWins(0).foundedYear(2016)
                        .teamPrincipal("Ayao Komatsu").powerUnit("Ferrari").isActive(true)
                        .description("The only American team on the current Formula 1 grid.")
                        .build(),

                Team.builder().name("RB").fullName("Visa Cash App RB F1 Team")
                        .nationality("Italian").baseLocation("Faenza, Italy")
                        .constructorChampionships(0).raceWins(1).foundedYear(2006)
                        .teamPrincipal("Alan Permane").powerUnit("Honda RBPT").isActive(true)
                        .description("Red Bull's sister team, formerly known as Toro Rosso and AlphaTauri, focused on developing young talent like Yuki Tsunoda.")
                        .build(),

                Team.builder().name("Kick Sauber").fullName("Stake F1 Team Kick Sauber")
                        .nationality("Swiss").baseLocation("Hinwil, Switzerland")
                        .constructorChampionships(0).raceWins(1).foundedYear(1993)
                        .teamPrincipal("Jonathan Wheatley").powerUnit("Ferrari").isActive(true)
                        .description("Transitioning into the Audi works team from 2026 onwards.")
                        .build()
        );

        teamRepository.saveAll(teams);
        log.info("  → {} teams saved.", teams.size());
    }

    private void seedDrivers() {
        List<Driver> drivers = List.of(
                // Red Bull Racing
                Driver.builder().name("Max Verstappen").nationality("Dutch").driverNumber(1)
                        .teamName("Red Bull Racing").worldChampionships(4).careerWins(71)
                        .careerPodiums(127).careerPoles(48).careerPoints(3444.5).debutYear(2015)
                        .isActive(true).biography("Four-time World Champion (2021–2024). One of the most dominant drivers of the modern era.")
                        .build(),
                Driver.builder().name("Yuki Tsunoda").nationality("Japanese").driverNumber(22)
                        .teamName("Red Bull Racing").worldChampionships(0).careerWins(0)
                        .careerPodiums(0).careerPoles(0).careerPoints(124.0).debutYear(2021)
                        .isActive(true).biography("Japanese driver known for his aggressive style. Promoted within the Red Bull system.")
                        .build(),

                // Ferrari
                Driver.builder().name("Charles Leclerc").nationality("Monegasque").driverNumber(16)
                        .teamName("Ferrari").worldChampionships(0).careerWins(8)
                        .careerPodiums(51).careerPoles(27).careerPoints(1672.0).debutYear(2018)
                        .isActive(true).biography("Ferrari driver known for exceptional qualifying speed.")
                        .build(),
                Driver.builder().name("Lewis Hamilton").nationality("British").driverNumber(44)
                        .teamName("Ferrari").worldChampionships(7).careerWins(105)
                        .careerPodiums(201).careerPoles(104).careerPoints(5018.5).debutYear(2007)
                        .isActive(true).biography("Seven-time World Champion and one of the most successful drivers in Formula 1 history.")
                        .build(),

                // Mercedes
                Driver.builder().name("George Russell").nationality("British").driverNumber(63)
                        .teamName("Mercedes").worldChampionships(0).careerWins(5)
                        .careerPodiums(24).careerPoles(7).careerPoints(1033.0).debutYear(2019)
                        .isActive(true).biography("Mercedes driver and consistent podium contender.")
                        .build(),
                Driver.builder().name("Kimi Antonelli").nationality("Italian").driverNumber(12)
                        .teamName("Mercedes").worldChampionships(0).careerWins(0)
                        .careerPodiums(3).careerPoles(0).careerPoints(150.0).debutYear(2025)
                        .isActive(true).biography("Highly rated Italian rookie entering Formula 1.")
                        .build(),

                // McLaren
                Driver.builder().name("Lando Norris").nationality("British").driverNumber(4)
                        .teamName("McLaren").worldChampionships(0).careerWins(11)
                        .careerPodiums(44).careerPoles(16).careerPoints(1430.0).debutYear(2019)
                        .isActive(true).biography("McLaren driver who secured his first win in 2024.")
                        .build(),
                Driver.builder().name("Oscar Piastri").nationality("Australian").driverNumber(81)
                        .teamName("McLaren").worldChampionships(0).careerWins(9)
                        .careerPodiums(26).careerPoles(6).careerPoints(802.0).debutYear(2023)
                        .isActive(true).biography("Talented young driver and multiple race winner.")
                        .build(),

                // Aston Martin
                Driver.builder().name("Fernando Alonso").nationality("Spanish").driverNumber(14)
                        .teamName("Aston Martin").worldChampionships(2).careerWins(32)
                        .careerPodiums(106).careerPoles(22).careerPoints(2393.0).debutYear(2001)
                        .isActive(true).biography("Two-time World Champion with a long and successful career.")
                        .build(),
                Driver.builder().name("Lance Stroll").nationality("Canadian").driverNumber(18)
                        .teamName("Aston Martin").worldChampionships(0).careerWins(0)
                        .careerPodiums(3).careerPoles(1).careerPoints(325.0).debutYear(2017)
                        .isActive(true).biography("Consistent midfield driver.")
                        .build(),

                // Alpine
                Driver.builder().name("Pierre Gasly").nationality("French").driverNumber(10)
                        .teamName("Alpine").worldChampionships(0).careerWins(1)
                        .careerPodiums(5).careerPoles(0).careerPoints(458.0).debutYear(2017)
                        .isActive(true).biography("Race winner with strong midfield performances.")
                        .build(),
                Driver.builder().name("Franco Colapinto").nationality("Argentinian").driverNumber(7)
                        .teamName("Alpine").worldChampionships(0).careerWins(0)
                        .careerPodiums(0).careerPoles(0).careerPoints(5.0).debutYear(2024)
                        .isActive(true).biography("Franco Colapinto replaced Australian Jack Doohan " +
                                "from the 7th race onwards..")
                        .build(),

                // Williams
                Driver.builder().name("Alexander Albon").nationality("Thai").driverNumber(23)
                        .teamName("Williams").worldChampionships(0).careerWins(0)
                        .careerPodiums(2).careerPoles(0).careerPoints(313.0).debutYear(2019)
                        .isActive(true).biography("Williams team leader known for strong race pace.")
                        .build(),
                Driver.builder().name("Carlos Sainz").nationality("Spanish").driverNumber(55)
                        .teamName("Williams").worldChampionships(0).careerWins(4)
                        .careerPodiums(29).careerPoles(6).careerPoints(1091.0).debutYear(2015)
                        .isActive(true).biography("Race winner who moved to Williams for a new challenge.")
                        .build(),

                // Haas
                Driver.builder().name("Esteban Ocon").nationality("French").driverNumber(31)
                        .teamName("Haas").worldChampionships(0).careerWins(1)
                        .careerPodiums(4).careerPoles(0).careerPoints(435.0).debutYear(2016)
                        .isActive(true).biography("Experienced driver contributing valuable points.")
                        .build(),
                Driver.builder().name("Oliver Bearman").nationality("British").driverNumber(87)
                        .teamName("Haas").worldChampionships(0).careerWins(0)
                        .careerPodiums(0).careerPoles(0).careerPoints(48.0).debutYear(2024)
                        .isActive(true).biography("Young Ferrari academy driver.")
                        .build(),

                // RB
                Driver.builder().name("Isack Hadjar").nationality("French").driverNumber(6)
                        .teamName("RB").worldChampionships(0).careerWins(0)
                        .careerPodiums(1).careerPoles(0).careerPoints(51.0).debutYear(2025)
                        .isActive(true).biography("Rookie driver from the Red Bull junior program.")
                        .build(),
                Driver.builder().name("Liam Lawson").nationality("Australian").driverNumber(3)
                        .teamName("RB").worldChampionships(0).careerWins(0)
                        .careerPodiums(0).careerPoles(3).careerPoints(44.0).debutYear(2023)
                        .isActive(true).biography("Experienced race winner and fan favorite.")
                        .build(),

                // Kick Sauber
                Driver.builder().name("Nico Hulkenberg").nationality("German").driverNumber(27)
                        .teamName("Kick Sauber").worldChampionships(0).careerWins(0)
                        .careerPodiums(1).careerPoles(1).careerPoints(622.0).debutYear(2010)
                        .isActive(true).biography("Highly experienced and consistent driver.")
                        .build(),
                Driver.builder().name("Gabriel Bortoleto").nationality("Brazilian").driverNumber(5)
                        .teamName("Kick Sauber").worldChampionships(0).careerWins(0)
                        .careerPodiums(0).careerPoles(0).careerPoints(19.0).debutYear(2025)
                        .isActive(true).biography("Promising rookie entering Formula 1.")
                        .build()
        );

        driverRepository.saveAll(drivers);
        log.info("  → {} drivers saved.", drivers.size());
    }

    private void seedRaces2025() {
        List<Race> races = List.of(
                Race.builder().name("Australian Grand Prix").circuitName("Albert Park Circuit")
                        .country("Australia").city("Melbourne").raceDate(LocalDate.of(2025, 3, 16))
                        .season(2025).roundNumber(1).isCompleted(true)
                        .winnerName("Lando Norris").winnerTeam("McLaren")
                        .fastestLapDriver("Lando Norris").fastestLapTime("1:22.167")
                        .totalLaps(58).circuitLengthKm(5.278).build(),

                Race.builder().name("Chinese Grand Prix").circuitName("Shanghai International Circuit")
                        .country("China").city("Shanghai").raceDate(LocalDate.of(2025, 3, 23))
                        .season(2025).roundNumber(2).isCompleted(true)
                        .winnerName("Oscar Piastri").winnerTeam("McLaren")
                        .fastestLapDriver("Lando Norris").fastestLapTime("1:35.454")
                        .totalLaps(56).circuitLengthKm(5.451).build(),

                Race.builder().name("Japanese Grand Prix").circuitName("Suzuka Circuit")
                        .country("Japan").city("Suzuka").raceDate(LocalDate.of(2025, 4, 6))
                        .season(2025).roundNumber(3).isCompleted(true)
                        .winnerName("Max Verstappen").winnerTeam("Red Bull Racing")
                        .fastestLapDriver("Kimi Antonelli").fastestLapTime("1:30.965")
                        .totalLaps(53).circuitLengthKm(5.807).build(),

                Race.builder().name("Bahrain Grand Prix").circuitName("Bahrain International Circuit")
                        .country("Bahrain").city("Sakhir").raceDate(LocalDate.of(2025, 4, 13))
                        .season(2025).roundNumber(4).isCompleted(true)
                        .winnerName("Oscar Piastri").winnerTeam("McLaren")
                        .fastestLapDriver("Oscar Piastri").fastestLapTime("1:35.140")
                        .totalLaps(57).circuitLengthKm(5.412).build(),

                Race.builder().name("Saudi Arabian Grand Prix").circuitName("Jeddah Corniche Circuit")
                        .country("Saudi Arabia").city("Jeddah").raceDate(LocalDate.of(2025, 4, 20))
                        .season(2025).roundNumber(5).isCompleted(true)
                        .winnerName("Oscar Piastri").winnerTeam("McLaren")
                        .fastestLapDriver("Lando Norris").fastestLapTime("1:31.778")
                        .totalLaps(50).circuitLengthKm(6.174).build(),

                Race.builder().name("Miami Grand Prix").circuitName("Miami International Autodrome")
                        .country("USA").city("Miami").raceDate(LocalDate.of(2025, 5, 4))
                        .season(2025).roundNumber(6).isCompleted(true)
                        .winnerName("Oscar Piastri").winnerTeam("McLaren")
                        .fastestLapDriver("Lando Norris").fastestLapTime("1:29.746")
                        .totalLaps(57).circuitLengthKm(5.412).build(),

                Race.builder().name("Emilia Romagna Grand Prix").circuitName("Imola")
                        .country("Italy").city("Imola").raceDate(LocalDate.of(2025, 5, 18))
                        .season(2025).roundNumber(7).isCompleted(true)
                        .winnerName("Max Verstappen").winnerTeam("Red Bull Racing")
                        .fastestLapDriver("Max Verstappen").fastestLapTime("1:17.988")
                        .totalLaps(63).circuitLengthKm(4.909).build(),

                Race.builder().name("Monaco Grand Prix").circuitName("Circuit de Monaco")
                        .country("Monaco").city("Monte Carlo").raceDate(LocalDate.of(2025, 5, 25))
                        .season(2025).roundNumber(8).isCompleted(true)
                        .winnerName("Lando Norris").winnerTeam("Ferrari")
                        .fastestLapDriver("Lando Norris").fastestLapTime("1:13.221")
                        .totalLaps(78).circuitLengthKm(3.337).build(),

                Race.builder().name("Spanish Grand Prix").circuitName("Barcelona-Catalunya")
                        .country("Spain").city("Barcelona").raceDate(LocalDate.of(2025, 6, 1))
                        .season(2025).roundNumber(9).isCompleted(true)
                        .winnerName("Oscar Piastri").winnerTeam("Red Bull Racing")
                        .fastestLapDriver("Oscar Piastri").fastestLapTime("1:15.743")
                        .totalLaps(66).circuitLengthKm(4.657).build(),

                Race.builder().name("Canadian Grand Prix").circuitName("Circuit Gilles Villeneuve")
                        .country("Canada").city("Montreal").raceDate(LocalDate.of(2025, 6, 15))
                        .season(2025).roundNumber(10).isCompleted(true)
                        .winnerName("George Russell").winnerTeam("Mercedes")
                        .fastestLapDriver("George Russell").fastestLapTime("1:14.119")
                        .totalLaps(70).circuitLengthKm(4.361).build(),

                Race.builder().name("Austrian Grand Prix").circuitName("Redbull Ring")
                        .country("Austria").city("Spielberg").raceDate(LocalDate.of(2025, 6, 29))
                        .season(2025).roundNumber(11).isCompleted(true)
                        .winnerName("Lando Norris").winnerTeam("McLaren")
                        .fastestLapDriver("Oscar Piastri").fastestLapTime("1:07.924")
                        .totalLaps(71).circuitLengthKm(4.326).build(),

                Race.builder().name("British Grand Prix").circuitName("Silverstone")
                        .country("UK").city("Silverstone").raceDate(LocalDate.of(2025, 7, 6))
                        .season(2025).roundNumber(12).isCompleted(true)
                        .winnerName("Lando Norris").winnerTeam("Ferrari")
                        .fastestLapDriver("Oscar Piastri").fastestLapTime("1:29.337")
                        .totalLaps(52).circuitLengthKm(5.891).build(),

                Race.builder().name("Belgian Grand Prix").circuitName("Spa-Francorchamps")
                        .country("Belgium").city("Stavelot").raceDate(LocalDate.of(2025, 7, 27))
                        .season(2025).roundNumber(13).isCompleted(true)
                        .winnerName("Oscar Piastri").winnerTeam("McLaren")
                        .fastestLapDriver("Kimi Antonelli").fastestLapTime("1:44.861")
                        .totalLaps(44).circuitLengthKm(7.004).build(),


                Race.builder().name("Hungarian Grand Prix").circuitName("Hungaroring")
                        .country("Hungary").city("Mogyoród,").raceDate(LocalDate.of(2025, 8, 3))
                        .season(2025).roundNumber(14).isCompleted(true)
                        .winnerName("Lando Norris").winnerTeam("McLaren")
                        .fastestLapDriver("George Russell").fastestLapTime("1:19.409")
                        .totalLaps(70).circuitLengthKm(4.381).build(),


                Race.builder().name("Dutch Grand Prix").circuitName("Zandvoort")
                        .country("Netherlands").city("Zandvoort").raceDate(LocalDate.of(2025, 8, 31))
                        .season(2025).roundNumber(15).isCompleted(true)
                        .winnerName("Oscar Piastri").winnerTeam("McLaren")
                        .fastestLapDriver("Oscar Piastri").fastestLapTime("1:12.271")
                        .totalLaps(72).circuitLengthKm(4.259).build(),

                Race.builder().name("Italian Grand Prix").circuitName("Autodromo Nazionale di Monza ")
                        .country("Italia").city("Monza").raceDate(LocalDate.of(2025, 9, 7))
                        .season(2025).roundNumber(16).isCompleted(true)
                        .winnerName("Max Verstappen").winnerTeam("Red Bull Racing")
                        .fastestLapDriver("Lando Norris").fastestLapTime("1:20.901")
                        .totalLaps(53).circuitLengthKm(5.793).build(),

                Race.builder().name("Azerbaijan Grand Prix").circuitName("Baku City Circuit")
                        .country("Azerbaijan").city("Baku").raceDate(LocalDate.of(2025, 9, 21))
                        .season(2025).roundNumber(17).isCompleted(true)
                        .winnerName("Max Verstappen").winnerTeam("Red Bull Racing")
                        .fastestLapDriver("Max Verstappen").fastestLapTime("1:43.388")
                        .totalLaps(51).circuitLengthKm(6.003).build(),

                Race.builder().name("Singapore Grand Prix").circuitName("Marina Bay Street Circuit")
                        .country("Singapore").city("Marina Bay").raceDate(LocalDate.of(2025, 10, 5))
                        .season(2025).roundNumber(18).isCompleted(true)
                        .winnerName("George Russell").winnerTeam("Mercedes")
                        .fastestLapDriver("Lewis Hamilton").fastestLapTime("1:33.808")
                        .totalLaps(62).circuitLengthKm(4.927).build(),

                Race.builder().name("United States Grand Prix").circuitName("Circuit of the Americas")
                        .country("United States").city("Austin").raceDate(LocalDate.of(2025, 10, 19))
                        .season(2025).roundNumber(19).isCompleted(true)
                        .winnerName("Max Verstappen").winnerTeam("Red Bull Racing")
                        .fastestLapDriver("Kimi Antonelli").fastestLapTime("1:37.577")
                        .totalLaps(56).circuitLengthKm(5.513).build(),

                Race.builder().name("Mexico City Grand Prix").circuitName("Autódromo Hermanos Rodríguez")
                        .country("Mexico").city("Mexico City").raceDate(LocalDate.of(2025, 10, 26))
                        .season(2025).roundNumber(20).isCompleted(true)
                        .winnerName("Lando Norris").winnerTeam("McLaren")
                        .fastestLapDriver("George Russell").fastestLapTime("1:20.052")
                        .totalLaps(71).circuitLengthKm(4.304).build(),

                Race.builder().name("Sao Paulo Grand Prix").circuitName("Autódromo José Carlos Pace")
                        .country("Brazil").city("Sao Paulo").raceDate(LocalDate.of(2025, 11, 9))
                        .season(2025).roundNumber(21).isCompleted(true)
                        .winnerName("Lando Norris").winnerTeam("McLaren")
                        .fastestLapDriver("Alexander Albon").fastestLapTime("1:12.400")
                        .totalLaps(72).circuitLengthKm(4.309).build(),

                Race.builder().name("Las Vegas Grand Prix").circuitName("Las Vegas Strip Circuit")
                        .country("United States").city("Nevada").raceDate(LocalDate.of(2025, 11, 22))
                        .season(2025).roundNumber(22).isCompleted(true)
                        .winnerName("Max Verstappen").winnerTeam("Red Bull Racing")
                        .fastestLapDriver("Max Verstappen").fastestLapTime("1:33.365")
                        .totalLaps(50).circuitLengthKm(6.201).build(),

                Race.builder().name("Qatar Grand Prix").circuitName("Lusail International Circuit")
                        .country("Qatar").city("Lusail").raceDate(LocalDate.of(2025, 11, 30))
                        .season(2025).roundNumber(23).isCompleted(true)
                        .winnerName("Max Verstappen").winnerTeam("Red Bull Racing")
                        .fastestLapDriver("Oscar Piastri").fastestLapTime("1:22.996")
                        .totalLaps(57).circuitLengthKm(5.419).build(),

                Race.builder().name("Abu Dhabi Grand Prix").circuitName("Yas Marina Circuit")
                        .country("UAE").city("Abu Dhabi").raceDate(LocalDate.of(2025, 12, 7))
                        .season(2025).roundNumber(24).isCompleted(true)
                        .winnerName("Max Verstappen").winnerTeam("Red Bull Racing")
                        .fastestLapDriver("Charles Leclerc").fastestLapTime("1:26.725")
                        .totalLaps(58).circuitLengthKm(5.281).build()
        );

        raceRepository.saveAll(races);
        log.info("  → {} races saved (2025 season completed).", races.size());
    }
}


