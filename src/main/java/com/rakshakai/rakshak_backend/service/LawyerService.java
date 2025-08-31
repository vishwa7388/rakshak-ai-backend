package com.rakshakai.rakshak_backend.service;

import com.rakshakai.rakshak_backend.dto.LawyerProfileDto;
import com.rakshakai.rakshak_backend.model.LawyerProfile;
import com.rakshakai.rakshak_backend.model.User;
import com.rakshakai.rakshak_backend.repository.LawyerProfileRepository;
import com.rakshakai.rakshak_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class LawyerService {

    @Autowired
    private LawyerProfileRepository lawyerProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    // Register new lawyer
    public LawyerProfileDto registerLawyer(LawyerProfileDto profileDto, Long userId) {
        // Check if user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Check if lawyer profile already exists
        if(lawyerProfileRepository.findByUserId(userId).isPresent()) {
            throw new RuntimeException("Lawyer profile already exists for this user");
        }

        // Create new lawyer profile
        LawyerProfile lawyerProfile = new LawyerProfile();
        lawyerProfile.setUser(user);
        lawyerProfile.setBarCouncilNumber(profileDto.getBarCouncilNumber());
        lawyerProfile.setSpecializations(profileDto.getSpecializations());
        lawyerProfile.setExperienceYears(profileDto.getExperienceYears());
        lawyerProfile.setConsultationFee(profileDto.getConsultationFee());
        lawyerProfile.setOfficeAddress(profileDto.getOfficeAddress());
        lawyerProfile.setCity(profileDto.getCity());
        lawyerProfile.setState(profileDto.getState());
        lawyerProfile.setLanguages(profileDto.getLanguages());
        lawyerProfile.setAvailabilityStatus("AVaiLABLE");
        lawyerProfile.setVerified(false); // Will be verified by admin
        lawyerProfile.setRating(0.0);
        lawyerProfile.setTotalCases(0);
        lawyerProfile.setSuccessRate(0.0);

        LawyerProfile savedProfile = lawyerProfileRepository.save(lawyerProfile);

        // Update user type to LAWYER
        user.setUserType("LAWYER");
        userRepository.save(user);

        return convertToDto(savedProfile);
    }

    // Advanced search with filters
    public List<LawyerProfileDto> searchLawyers(String city, String specialization,
                                                Double minRating, Double maxFee,
                                                Integer minExperience, Boolean verifiedOnly) {

        List<LawyerProfile> lawyers = lawyerProfileRepository.findAll();

        // Apply filters
        return lawyers.stream()
                .filter(lawyer -> city == null || lawyer.getCity().equalsIgnoreCase(city))
                .filter(lawyer -> specialization == null ||
                        (lawyer.getSpecializations() != null &&
                                lawyer.getSpecializations().contains(specialization)))
                .filter(lawyer -> minRating == null || lawyer.getRating() >= minRating)
                .filter(lawyer -> maxFee == null || lawyer.getConsultationFee() <= maxFee)
                .filter(lawyer -> minExperience == null || lawyer.getExperienceYears() >= minExperience)
                .filter(lawyer -> !verifiedOnly || lawyer.getVerified())
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Get top rated lawyers with pagination
    public Page<LawyerProfileDto> getTopRatedLawyers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("rating").descending());
        Page<LawyerProfile> lawyerPage = lawyerProfileRepository.findByIsVerified(true, pageable);
        return lawyerPage.map(this::convertToDto);
    }

    // Get lawyer recommendations based on case type
    public List<LawyerProfileDto> getRecommendedLawyers(String caseType, String userCity) {
        String specialization = mapCaseTypeToSpecialization(caseType);

        // First try to find lawyers in the same city
        List<LawyerProfile> cityLawyers = lawyerProfileRepository.findByCity(userCity);

        List<LawyerProfile> recommended = cityLawyers.stream()
                .filter(l -> l.getVerified())
                .filter(l -> l.getSpecializations() != null &&
                        l.getSpecializations().contains(specialization))
                .sorted((l1, l2) -> {
                    // Sort by rating and success rate
                    double score1 = l1.getRating() * 0.6 + (l1.getSuccessRate() / 100) * 0.4;
                    double score2 = l2.getRating() * 0.6 + (l2.getSuccessRate() / 100) * 0.4;
                    return Double.compare(score2, score1);
                })
                .limit(5)
                .collect(Collectors.toList());

        return recommended.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // Verify lawyer (admin function)
    public LawyerProfileDto verifyLawyer(Long lawyerId, String barCouncilVerificationStatus) {
        LawyerProfile lawyer = lawyerProfileRepository.findById(lawyerId)
                .orElseThrow(() -> new RuntimeException("Lawyer not found with ID: " + lawyerId));

        // In real app, this would check with Bar Council API
        if("VERIFIED".equals(barCouncilVerificationStatus)) {
            lawyer.setVerified(true);
            LawyerProfile savedLawyer = lawyerProfileRepository.save(lawyer);

            // Notify the lawyer
            notificationService.notifyUser(lawyer.getUser(),
                    "Congratulations! Your lawyer profile has been verified.");

            return convertToDto(savedLawyer);
        } else {
            throw new RuntimeException("Bar Council verification failed");
        }
    }

    // Update lawyer availability
    public LawyerProfileDto updateAvailability(Long lawyerId, String availabilityStatus) {
        LawyerProfile lawyer = lawyerProfileRepository.findById(lawyerId)
                .orElseThrow(() -> new RuntimeException("Lawyer not found with ID: " + lawyerId));

        lawyer.setAvailabilityStatus(availabilityStatus);
        LawyerProfile savedLawyer = lawyerProfileRepository.save(lawyer);

        return convertToDto(savedLawyer);
    }

    // Update lawyer stats after case completion
    public void updateLawyerStats(Long lawyerId, boolean caseWon) {
        LawyerProfile lawyer = lawyerProfileRepository.findById(lawyerId)
                .orElseThrow(() -> new RuntimeException("Lawyer not found with ID: " + lawyerId));

        int totalCases = lawyer.getTotalCases() + 1;
        lawyer.setTotalCases(totalCases);

        if(caseWon) {
            double currentSuccessRate = lawyer.getSuccessRate();
            double wonCases = (currentSuccessRate * (totalCases - 1) / 100) + 1;
            double newSuccessRate = (wonCases / totalCases) * 100;
            lawyer.setSuccessRate(newSuccessRate);
        } else {
            double currentSuccessRate = lawyer.getSuccessRate();
            double wonCases = currentSuccessRate * (totalCases - 1) / 100;
            double newSuccessRate = (wonCases / totalCases) * 100;
            lawyer.setSuccessRate(newSuccessRate);
        }

        lawyerProfileRepository.save(lawyer);
    }

    // Helper method to map case types to lawyer specializations
    private String mapCaseTypeToSpecialization(String caseType) {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("ASSAULT", "Criminal Law");
        mapping.put("ROBBERY", "Criminal Law");
        mapping.put("FRAUD", "Criminal Law");
        mapping.put("PROPERTY_DISPUTE", "Property Law");
        mapping.put("DIVORCE", "Family Law");
        mapping.put("CUSTODY", "Family Law");
        mapping.put("CONTRACT", "Corporate Law");
        mapping.put("EMPLOYMENT", "Labor Law");

        return mapping.getOrDefault(caseType.toUpperCase(), "General Practice");
    }

    // Convert entity to DTO
    private LawyerProfileDto convertToDto(LawyerProfile lawyerProfile) {
        LawyerProfileDto dto = new LawyerProfileDto();
        dto.setId(lawyerProfile.getId());
        dto.setUserId(lawyerProfile.getUser().getId());
        dto.setUserName(lawyerProfile.getUser().getName());
        dto.setUserEmail(lawyerProfile.getUser().getEmail());
        dto.setBarCouncilNumber(lawyerProfile.getBarCouncilNumber());
        dto.setSpecializations(lawyerProfile.getSpecializations());
        dto.setExperienceYears(lawyerProfile.getExperienceYears());
        dto.setConsultationFee(lawyerProfile.getConsultationFee());
        dto.setRating(lawyerProfile.getRating());
        dto.setTotalCases(lawyerProfile.getTotalCases());
        dto.setSuccessRate(lawyerProfile.getSuccessRate());
        dto.setOfficeAddress(lawyerProfile.getOfficeAddress());
        dto.setCity(lawyerProfile.getCity());
        dto.setState(lawyerProfile.getState());
        dto.setLanguages(lawyerProfile.getLanguages());
        dto.setAvailabilityStatus(lawyerProfile.getAvailabilityStatus());
        dto.setVerified(lawyerProfile.getVerified());

        return dto;
    }

    // Get lawyer analytics
    public Map<String, Object> getLawyerAnalytics(Long lawyerId) {
        LawyerProfile lawyer = lawyerProfileRepository.findById(lawyerId)
                .orElseThrow(() -> new RuntimeException("Lawyer not found"));

        Map<String, Object> analytics = new HashMap<>();
        analytics.put("totalCases", lawyer.getTotalCases());
        analytics.put("successRate", lawyer.getSuccessRate());
        analytics.put("rating", lawyer.getRating());
        analytics.put("monthlyEarnings", calculateMonthlyEarnings(lawyer));
        analytics.put("ranking", calculateLawyerRanking(lawyer));

        return analytics;
    }

    private Double calculateMonthlyEarnings(LawyerProfile lawyer) {
        // This would connect to payment/billing service
        // For now, returning estimated earnings
        return lawyer.getConsultationFee() * lawyer.getTotalCases() * 0.1; // Rough estimate
    }

    private Integer calculateLawyerRanking(LawyerProfile lawyer) {
        List<LawyerProfile> allLawyers = lawyerProfileRepository.findByCity(lawyer.getCity());
        allLawyers.sort((l1, l2) -> Double.compare(l2.getRating(), l1.getRating()));

        for(int i = 0; i < allLawyers.size(); i++) {
            if(allLawyers.get(i).getId().equals(lawyer.getId())) {
                return i + 1;
            }
        }
        return -1;
    }
}