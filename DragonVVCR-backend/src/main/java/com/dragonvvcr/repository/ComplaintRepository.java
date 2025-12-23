package com.dragonvvcr.repository;

import com.dragonvvcr.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    List<Complaint> findByUserId(Long userId);

    List<Complaint> findByTargetPlayerId(String targetPlayerId);

    List<Complaint> findByDungeonName(String dungeonName);

    @Query("SELECT c FROM Complaint c WHERE c.user.id = :userId AND c.targetPlayerId = :targetPlayerId")
    List<Complaint> findByUserIdAndTargetPlayerId(@Param("userId") Long userId, @Param("targetPlayerId") String targetPlayerId);

    @Query("SELECT c.targetPlayerId, COUNT(c) as count FROM Complaint c GROUP BY c.targetPlayerId ORDER BY count DESC")
    List<Object[]> findMostComplainedPlayers();

    @Query("SELECT c.dungeonName, COUNT(c) as count FROM Complaint c GROUP BY c.dungeonName ORDER BY count DESC")
    List<Object[]> findMostComplainedDungeons();
}