package com.rakshakai.rakshak_backend.service;

import com.rakshakai.rakshak_backend.dto.EmergencyCaseDto;
import com.rakshakai.rakshak_backend.model.EmergencyCase;
import com.rakshakai.rakshak_backend.model.User;
import com.rakshakai.rakshak_backend.repository.EmergencyCaseRepository;
import com.rakshakai.rakshak_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmergencyCaseService {

    @Autowired
    private EmergencyCaseRepository emergencyCaseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    // Create new emergency case
    public EmergencyCaseDto createEmergencyCase(EmergencyCaseDto caseDto, Long reporterId) {
        // Step 1: Find the reporter
        User reporter = userRepository.findById(reporterId)
                .orElseThrow(() -> new RuntimeException("Reporter not found with ID: " + reporterId));

        // Step 2: Create new emergency case
        EmergencyCase emergencyCase = new EmergencyCase();
        emergencyCase.setCaseNumber(generateCaseNumber());
        emergencyCase.setReporter(reporter);
        emergencyCase.setIncidentType(caseDto.getIncidentType());
        emergencyCase.setDescription(caseDto.getDescription());
        emergencyCase.setLocation(caseDto.getLocation());
        emergencyCase.setLatitude(caseDto.getLatitude());
        emergencyCase.setLongitude(caseDto.getLongitude());
        emergencyCase.setStatus("OPEN");
        emergencyCase.setPriorityLevel(determinePriority(caseDto.getIncidentType()));
        emergencyCase.setCreatedAt(LocalDateTime.now());

        // Step 3: Save to database
        EmergencyCase savedCase = emergencyCaseRepository.save(emergencyCase);

        // Step 4: Send notifications to nearby police stations
        if(emergencyCase.getPriorityLevel().equals("HIGH")) {
            notificationService.alertNearbyPoliceStations(savedCase);
        }

        // Step 5: Convert to DTO and return
        return convertToDto(savedCase);
    }

    // Get all high priority cases
    public List<EmergencyCaseDto> getHighPriorityCases() {
        return emergencyCaseRepository.findHighPriorityOpenCases()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Get cases by reporter
    public List<EmergencyCaseDto> getCasesByReporter(Long reporterId) {
        return emergencyCaseRepository.findByReporterId(reporterId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Update case status
    public EmergencyCaseDto updateCaseStatus(Long caseId, String newStatus, String assignedOfficer) {
        EmergencyCase emergencyCase = emergencyCaseRepository.findById(caseId)
                .orElseThrow(() -> new RuntimeException("Case not found with ID: " + caseId));

        emergencyCase.setStatus(newStatus);
        emergencyCase.setUpdatedAt(LocalDateTime.now());

        if(assignedOfficer != null) {
            emergencyCase.setAssignedOfficer(assignedOfficer);
        }

        EmergencyCase updatedCase = emergencyCaseRepository.save(emergencyCase);

        // Notify reporter about status change
        notificationService.notifyReporter(updatedCase.getReporter(),
                "Your case " + updatedCase.getCaseNumber() + " status updated to: " + newStatus);

        return convertToDto(updatedCase);
    }

    // Get cases within radius (for police dashboard)
    public List<EmergencyCaseDto> getCasesNearLocation(Double latitude, Double longitude, Double radiusKm) {
        List<EmergencyCase> allCases = emergencyCaseRepository.findByStatus("OPEN");

        return allCases.stream()
                .filter(c -> calculateDistance(latitude, longitude, c.getLatitude(), c.getLongitude()) <= radiusKm)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Helper method to generate unique case number
    private String generateCaseNumber() {
        String prefix = "CASE";
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(7);
        return prefix + timestamp;
    }

    // Helper method to determine priority based on incident type
    private String determinePriority(String incidentType) {
        if(incidentType == null) return "MEDIUM";

        switch(incidentType.toUpperCase()) {
            case "ASSAULT":
            case "ROBBERY":
            case "KIDNAPPING":
            case "MURDER":
                return "HIGH";
            case "THEFT":
            case "FRAUD":
            case "VANDALISM":
                return "MEDIUM";
            default:
                return "LOW";
        }
    }

    // Helper method to calculate distance between two coordinates
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance in km
    }

    // Convert entity to DTO
    private EmergencyCaseDto convertToDto(EmergencyCase emergencyCase) {
        return new EmergencyCaseDto(
                emergencyCase.getId(),
                emergencyCase.getCaseNumber(),
                emergencyCase.getReporter() != null ? emergencyCase.getReporter().getName() : null,
                emergencyCase.getReporter() != null ? emergencyCase.getReporter().getEmail() : null,
                emergencyCase.getIncidentType(),
                emergencyCase.getDescription(),
                emergencyCase.getLocation(),
                emergencyCase.getLatitude(),
                emergencyCase.getLongitude(),
                emergencyCase.getStatus(),
                emergencyCase.getPriorityLevel(),
                emergencyCase.getCreatedAt(),
                emergencyCase.getAssignedOfficer()
        );
    }

    // Get dashboard statistics
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalCases", emergencyCaseRepository.count());
        stats.put("openCases", emergencyCaseRepository.countByStatus("OPEN"));
        stats.put("highPriorityCases", emergencyCaseRepository.countByPriorityLevel("HIGH"));
        stats.put("resolvedToday", emergencyCaseRepository.countResolvedToday());

        return stats;
    }
}