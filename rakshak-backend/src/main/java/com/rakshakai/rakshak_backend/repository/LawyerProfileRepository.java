package com.rakshakai.rakshak_backend.repository;

import com.rakshakai.rakshak_backend.model.LawyerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LawyerProfileRepository extends JpaRepository<LawyerProfile, Long> {
    Optional<LawyerProfile> findByBarCouncilNumber(String barCouncilNumber);
    List<LawyerProfile> findByCity(String city);
    List<LawyerProfile> findBySpecializationsContaining(String specialization);
    List<LawyerProfile> findByIsVerified(Boolean verified);
    List<LawyerProfile> findByAvailabilityStatus(String status);

    @Query("SELECT l FROM LawyerProfile l WHERE l.city = ?1 AND l.specializations LIKE %?2%")
    List<LawyerProfile> findVerifiedLawyersByCityAndSpecialization(String city, String specialization);

    @Query("SELECT l FROM LawyerProfile l WHERE l.rating >= ?1 AND l.isVerified = true ORDER BY l.rating DESC")
    List<LawyerProfile> findTopRatedLawyers(Double minRating);
}