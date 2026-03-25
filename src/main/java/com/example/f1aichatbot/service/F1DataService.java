package com.example.f1aichatbot.service;

import com.example.f1aichatbot.model.Driver;
import com.example.f1aichatbot.model.Race;
import com.example.f1aichatbot.model.Team;
import com.example.f1aichatbot.repository.DriverRepository;
import com.example.f1aichatbot.repository.RaceRepository;
import com.example.f1aichatbot.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class F1DataService {

    private final DriverRepository driverRepository;
    private final TeamRepository teamRepository;
    private final RaceRepository raceRepository;

    // ===========================
    // Driver Operations
    // ===========================

    public List<Driver> getAllActiveDrivers() {
        return driverRepository.findByIsActiveTrue();
    }

    public Optional<Driver> findDriverByName(String name) {
        return driverRepository.findByNameIgnoreCase(name);
    }

    public List<Driver> searchDrivers(String keyword) {
        return driverRepository.searchByName(keyword);
    }

    public List<Driver> getDriversByTeam(String teamName) {
        return driverRepository.findByTeamNameIgnoreCase(teamName);
    }

    public List<Driver> getChampions() {
        return driverRepository.findByWorldChampionshipsGreaterThan(0);
    }

    // ===========================
    // Team Operations
    // ===========================

    public List<Team> getAllActiveTeams() {
        return teamRepository.findByIsActiveTrue();
    }

    public Optional<Team> findTeamByName(String name) {
        return teamRepository.findByNameIgnoreCase(name);
    }

    public List<Team> getTeamsOrderedByChampionships() {
        return teamRepository.findAllOrderByChampionships();
    }

    // ===========================
    // Race Operations
    // ===========================

    public List<Race> getRacesBySeason(int season) {
        return raceRepository.findScheduleBySeason(season);
    }

    public List<Race> getUpcomingRaces() {
        return raceRepository.findUpcomingRaces();
    }

    public List<Race> getRacesByDriver(String driverName) {
        return raceRepository.findRacesByWinner(driverName);
    }

    public List<Integer> getAllSeasons() {
        return raceRepository.findAllSeasons();
    }

    // ===========================
    // Context Builder for Claude
    // ===========================

    /**
     * Kullanıcının sorusuna göre ilgili F1 verisini çekip
     * Claude'a context olarak verilecek metni oluşturur.
     */
    public String buildRelevantContext(String userMessage) {
        StringBuilder context = new StringBuilder();
        String lowerMessage = userMessage.toLowerCase();

        // Sürücü sorusu mu?
        if (containsDriverKeywords(lowerMessage)) {
            appendDriverContext(context, lowerMessage);
        }

        // Takım sorusu mu?
        if (containsTeamKeywords(lowerMessage)) {
            appendTeamContext(context, lowerMessage);
        }

        // Yarış/takvim sorusu mu?
        if (containsRaceKeywords(lowerMessage)) {
            appendRaceContext(context, lowerMessage);
        }

        // Genel F1 sorusu - tüm aktif veriyi özet ver
        if (context.length() == 0) {
            appendGeneralContext(context);
        }

        return context.toString();
    }

    private boolean containsDriverKeywords(String message) {
        return message.contains("sürücü") || message.contains("pilot") ||
                message.contains("driver") || message.contains("hamilton") ||
                message.contains("verstappen") || message.contains("leclerc") ||
                message.contains("norris") || message.contains("şampiyon") ||
                message.contains("champion") || message.contains("kazanan");
    }

    private boolean containsTeamKeywords(String message) {
        return message.contains("takım") || message.contains("team") ||
                message.contains("constructor") || message.contains("ferrari") ||
                message.contains("mercedes") || message.contains("red bull") ||
                message.contains("mclaren") || message.contains("alpine") ||
                message.contains("aston") || message.contains("williams");
    }

    private boolean containsRaceKeywords(String message) {
        return message.contains("yarış") || message.contains("race") ||
                message.contains("grand prix") || message.contains("gp") ||
                message.contains("takvim") || message.contains("schedule") ||
                message.contains("circuit") || message.contains("pist") ||
                message.contains("sezon") || message.contains("season");
    }

    private void appendDriverContext(StringBuilder ctx, String message) {
        ctx.append("\n=== SÜRÜCÜ BİLGİLERİ ===\n");

        // Spesifik sürücü araması
        List<String> driverKeywords = List.of("hamilton", "verstappen", "leclerc",
                "norris", "sainz", "russell", "alonso", "perez", "piastri", "stroll");

        boolean foundSpecific = false;
        for (String keyword : driverKeywords) {
            if (message.contains(keyword)) {
                List<Driver> found = driverRepository.searchByName(keyword);
                for (Driver d : found) {
                    ctx.append(formatDriver(d)).append("\n");
                }
                foundSpecific = true;
            }
        }

        if (!foundSpecific) {
            // Tüm aktif sürücüleri listele
            List<Driver> activeDrivers = driverRepository.findByIsActiveTrue();
            ctx.append("2025 Aktif Sürücüler:\n");
            for (Driver d : activeDrivers) {
                ctx.append(String.format("- #%d %s (%s) - %s\n",
                        d.getDriverNumber(), d.getName(), d.getNationality(), d.getTeamName()));
            }
        }
    }

    private void appendTeamContext(StringBuilder ctx, String message) {
        ctx.append("\n=== TAKIM BİLGİLERİ ===\n");

        List<String> teamKeywords = List.of("ferrari", "mercedes", "red bull", "mclaren",
                "alpine", "aston martin", "williams", "haas", "rb", "sauber", "kick");

        boolean foundSpecific = false;
        for (String keyword : teamKeywords) {
            if (message.contains(keyword)) {
                List<Team> found = teamRepository.searchByName(keyword);
                for (Team t : found) {
                    ctx.append(formatTeam(t)).append("\n");
                }
                foundSpecific = true;
            }
        }

        if (!foundSpecific) {
            List<Team> teams = teamRepository.findByIsActiveTrue();
            ctx.append("2025 F1 Takımları:\n");
            for (Team t : teams) {
                ctx.append(String.format("- %s | %d Şampiyonluk | Motor: %s\n",
                        t.getName(), t.getConstructorChampionships(), t.getPowerUnit()));
            }
        }
    }

    private void appendRaceContext(StringBuilder ctx, String message) {
        ctx.append("\n=== YARIŞ TAKVİMİ ===\n");

        // 2025 sezonu
        List<Race> races2025 = raceRepository.findScheduleBySeason(2025);
        if (!races2025.isEmpty()) {
            ctx.append("2025 F1 Sezonu:\n");
            for (Race r : races2025) {
                String status = Boolean.TRUE.equals(r.getIsCompleted()) ? "✓ TAMAMLANDI" : "⏳ BEKLIYOR";
                ctx.append(String.format("R%d: %s - %s, %s | %s%s\n",
                        r.getRoundNumber(), r.getName(), r.getCity(), r.getCountry(),
                        r.getRaceDate(), Boolean.TRUE.equals(r.getIsCompleted()) ?
                                " | Kazanan: " + r.getWinnerName() + " (" + r.getWinnerTeam() + ")" : ""));
            }
        }

        // Yaklaşan yarışlar
        List<Race> upcoming = raceRepository.findUpcomingRaces();
        if (!upcoming.isEmpty()) {
            ctx.append("\nYaklaşan Yarışlar:\n");
            upcoming.stream().limit(3).forEach(r ->
                    ctx.append(String.format("- %s | %s (%s)\n", r.getName(), r.getRaceDate(), r.getCountry()))
            );
        }
    }

    private void appendGeneralContext(StringBuilder ctx) {
        ctx.append("\n=== GENEL F1 ÖZET ===\n");
        long driverCount = driverRepository.findByIsActiveTrue().size();
        long teamCount = teamRepository.findByIsActiveTrue().size();
        ctx.append(String.format("2025 Sezonu: %d aktif sürücü, %d takım\n", driverCount, teamCount));

        ctx.append("\nEfsanevi Şampiyonlar:\n");
        driverRepository.findAllOrderByChampionships().stream()
                .filter(d -> d.getWorldChampionships() > 0)
                .limit(5)
                .forEach(d -> ctx.append(String.format("- %s: %d şampiyonluk\n",
                        d.getName(), d.getWorldChampionships())));
    }

    private String formatDriver(Driver d) {
        return String.format(
                "Sürücü: %s\n  Numara: #%d | Takım: %s | Uyruk: %s\n" +
                        "  Şampiyonluk: %d | Kazanma: %d | Podyum: %d | Pole: %d\n  Toplam Puan: %.1f\n",
                d.getName(), d.getDriverNumber(), d.getTeamName(), d.getNationality(),
                d.getWorldChampionships(), d.getCareerWins(), d.getCareerPodiums(),
                d.getCareerPoles(), d.getCareerPoints()
        );
    }

    private String formatTeam(Team t) {
        return String.format(
                "Takım: %s\n  Merkez: %s | Kuruluş: %d\n" +
                        "  Şampiyonluk: %d | Kazanma: %d | Motor: %s\n  Takım Başkanı: %s\n",
                t.getFullName(), t.getBaseLocation(), t.getFoundedYear(),
                t.getConstructorChampionships(), t.getRaceWins(), t.getPowerUnit(), t.getTeamPrincipal()
        );
    }
}
