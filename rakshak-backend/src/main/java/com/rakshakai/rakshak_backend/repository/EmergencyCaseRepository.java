package com.rakshakai.rakshak_backend.repository;

import com.rakshakai.rakshak_backend.model.EmergencyCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmergencyCaseRepository extends JpaRepository<EmergencyCase, Long> {
    List<EmergencyCase> findByStatus(String status);
    List<EmergencyCase> findByIncidentType(String incidentType);
    List<EmergencyCase> findByReporterId(Long reporterId);
    Optional<EmergencyCase> findByCaseNumber(String caseNumber);

    @Query("SELECT e FROM EmergencyCase e WHERE e.createdAt BETWEEN ?1 AND ?2")
    List<EmergencyCase> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT e FROM EmergencyCase e WHERE e.priorityLevel = 'HIGH' AND e.status != 'CLOSED'")
    List<EmergencyCase> findHighPriorityOpenCases();
}