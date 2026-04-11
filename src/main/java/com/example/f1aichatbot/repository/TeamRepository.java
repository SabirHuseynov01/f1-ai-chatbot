package com.example.f1aichatbot.repository;

import com.example.f1aichatbot.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findByIsActiveTrue();

    Optional<Team> findByNameIgnoreCase(String name);

    List<Team> findByNationalityIgnoreCase(String nationality);

    @Query("SELECT t FROM Team t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(t.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Team> searchByName(@Param("keyword") String keyword);

    @Query("SELECT t FROM Team t ORDER BY t.constructorChampionships DESC")
    List<Team> findAllOrderByChampionships();
}




