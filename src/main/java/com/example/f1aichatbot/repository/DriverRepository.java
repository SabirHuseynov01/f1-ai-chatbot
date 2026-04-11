package com.example.f1aichatbot.repository;

import com.example.f1aichatbot.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver,Long> {

    List<Driver> findByIsActiveTrue();

    Optional<Driver> findByNameIgnoreCase(String name);

    List<Driver> findByTeamNameIgnoreCase(String teamName);

    List<Driver> findByNationalityIgnoreCase(String nationality);

    @Query("SELECT d FROM Driver d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Driver> searchByName(@Param("keyword") String keyword);

    @Query("SELECT d FROM Driver d ORDER BY d.worldChampionships DESC")
    List<Driver> findAllOrderByChampionships();

    @Query("SELECT d FROM Driver d ORDER BY d.careerWins DESC")
    List<Driver> findAllOrderByWins();

    List<Driver> findByWorldChampionshipsGreaterThan(Integer championships);
}



