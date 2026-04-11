package com.example.f1aichatbot.repository;


import com.example.f1aichatbot.model.Race;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RaceRepository extends JpaRepository<Race, Long> {

    List<Race> findBySeason(Integer season);

    List<Race> findBySeasonOrderByRoundNumber(Integer season);

    List<Race> findByIsCompletedTrue();

    List<Race> findByIsCompletedFalse();

    @Query("SELECT r FROM Race r WHERE r.isCompleted = false ORDER BY r.raceDate ASC")
    List<Race> findUpcomingRaces();

    @Query("SELECT r FROM Race r WHERE r.season = :season ORDER BY r.roundNumber ASC")
    List<Race> findScheduleBySeason(@Param("season") Integer season);

    @Query("SELECT r FROM Race r WHERE LOWER(r.winnerName) LIKE LOWER(CONCAT('%', :driverName, '%'))")
    List<Race> findRacesByWinner(@Param("driverName") String driverName);

    @Query("SELECT r FROM Race r WHERE LOWER(r.circuitName) LIKE LOWER(CONCAT('%', :circuit, '%')) OR LOWER(r.country) LIKE LOWER(CONCAT('%', :circuit, '%'))")
    List<Race> findByCircuit(@Param("circuit") String circuit);

    @Query("SELECT DISTINCT r.season FROM Race r ORDER BY r.season DESC")
    List<Integer> findAllSeasons();
}



