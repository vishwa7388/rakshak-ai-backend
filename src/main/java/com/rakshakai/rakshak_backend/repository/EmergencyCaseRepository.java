package com.rakshakai.rakshak_backend.repository;

import com.rakshakai.rakshak_backend.model.EmergencyCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyCaseRepository extends JpaRepository<EmergencyCase, Long> {

    // Find high priority open cases
    @Query("SELECT e FROM EmergencyCase e WHERE e.priorityLevel = 'HIGH' AND e.status = 'OPEN' ORDER BY e.createdAt DESC")
    List<EmergencyCase> findHighPriorityOpenCases();

    // Find cases by reporter ID
    List<EmergencyCase> findByReporterId(Long reporterId);

    // Find cases by status
    List<EmergencyCase> findByStatus(String status);

    // Count cases by status
    Long countByStatus(String status);

    // Count cases by priority level
    Long countByPriorityLevel(String priorityLevel);

    // Count cases resolved today
    @Query("SELECT COUNT(e) FROM EmergencyCase e WHERE e.status = 'RESOLVED' AND DATE(e.updatedAt) = CURRENT_DATE")
    Long countResolvedToday();

    // Find cases within date range
    @Query("SELECT e FROM EmergencyCase e WHERE e.createdAt BETWEEN :startDate AND :endDate")
    List<EmergencyCase> findCasesInDateRange(@Param("startDate") java.time.LocalDateTime startDate,
                                             @Param("endDate") java.time.LocalDateTime endDate);
}
