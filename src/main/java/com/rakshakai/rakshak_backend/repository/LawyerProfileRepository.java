package com.rakshakai.rakshak_backend.repository;

import com.rakshakai.rakshak_backend.model.LawyerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LawyerProfileRepository extends JpaRepository<LawyerProfile, Long> {

    // Find by user ID
    Optional<LawyerProfile> findByUserId(Long userId);

    // Find by city
    List<LawyerProfile> findByCity(String city);

    // Find by verification status
    List<LawyerProfile> findByGetVerified(boolean isVerified);

    // Find by verification status with pagination
    Page<LawyerProfile> findByIsVerified(boolean isVerified, Pageable pageable);

    // Find top rated lawyers
    @Query("SELECT l FROM LawyerProfile l WHERE l.rating >= :minRating ORDER BY l.rating DESC")
    List<LawyerProfile> findTopRatedLawyers(@Param("minRating") Double minRating);

    // Find verified lawyers by city and specialization
    @Query("SELECT l FROM LawyerProfile l WHERE l.city = :city AND l.isVerified = true AND :specialization MEMBER OF l.specializations")
    List<LawyerProfile> findVerifiedLawyersByCityAndSpecialization(@Param("city") String city,
                                                                   @Param("specialization") String specialization);

    // Find available lawyers
    List<LawyerProfile> findByAvailabilityStatus(String availabilityStatus);

    // Search lawyers with multiple criteria
    @Query("SELECT l FROM LawyerProfile l WHERE " +
            "(:city IS NULL OR l.city = :city) AND " +
            "(:minRating IS NULL OR l.rating >= :minRating) AND " +
            "(:maxFee IS NULL OR l.consultationFee <= :maxFee) AND " +
            "(:isVerified IS NULL OR l.isVerified = :isVerified)")
    List<LawyerProfile> searchLawyers(@Param("city") String city,
                                      @Param("minRating") Double minRating,
                                      @Param("maxFee") Double maxFee,
                                      @Param("isVerified") Boolean isVerified);
}