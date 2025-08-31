package com.rakshakai.rakshak_backend.service;

import com.rakshakai.rakshak_backend.model.EmergencyCase;
import com.rakshakai.rakshak_backend.model.User;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    // Alert nearby police stations for emergency
    public void alertNearbyPoliceStations(EmergencyCase emergencyCase) {
        // In production, this would integrate with:
        // 1. Police station database
        // 2. SMS gateway (Twilio/AWS SNS)
        // 3. Push notifications (Firebase)

        logger.info("EMERGENCY ALERT: Case {} at location {}, {}",
                emergencyCase.getCaseNumber(),
                emergencyCase.getLatitude(),
                emergencyCase.getLongitude());

        // Simulate finding nearby stations
        List<String> nearbyStations = findNearbyPoliceStations(
                emergencyCase.getLatitude(),
                emergencyCase.getLongitude()
        );

        for(String station : nearbyStations) {
            sendEmergencyAlert(station, emergencyCase);
        }
    }

    // Notify user about updates
    public void notifyUser(User user, String message) {
        logger.info("Notification to {}: {}", user.getEmail(), message);

        // In production, send via:
        // - Email (AWS SES or SendGrid)
        // - SMS (Twilio)
        // - In-app notification
        // - Push notification
    }

    // Notify reporter about case updates
    public void notifyReporter(User reporter, String message) {
        if(reporter != null) {
            logger.info("Case update notification to reporter {}: {}",
                    reporter.getEmail(), message);
            // Send actual notification
        }
    }

    // Send emergency alert to police station
    private void sendEmergencyAlert(String policeStation, EmergencyCase emergencyCase) {
        String alertMessage = String.format(
                "🚨 EMERGENCY ALERT 🚨\n" +
                        "Case: %s\n" +
                        "Type: %s\n" +
                        "Priority: %s\n" +
                        "Location: %s\n" +
                        "Coordinates: %.6f, %.6f\n" +
                        "Description: %s\n" +
                        "Reporter: %s (%s)",
                emergencyCase.getCaseNumber(),
                emergencyCase.getIncidentType(),
                emergencyCase.getPriorityLevel(),
                emergencyCase.getLocation(),
                emergencyCase.getLatitude(),
                emergencyCase.getLongitude(),
                emergencyCase.getDescription(),
                emergencyCase.getReporter().getName(),
                emergencyCase.getReporter().getEmail()
        );

        logger.error(alertMessage); // Using error level for visibility
        // In production: Send to police system API
    }

    // Find nearby police stations (mock implementation)
    private List<String> findNearbyPoliceStations(Double latitude, Double longitude) {
        // In production: Query police station database with geo-coordinates
        // For now, return mock stations
        return Arrays.asList(
                "Meerut City Police Station",
                "Sadar Bazar Police Station",
                "Medical Police Station"
        );
    }

    // Send lawyer consultation request notification
    public void notifyLawyerAboutConsultation(Long lawyerId, String clientName, String caseType) {
        String message = String.format(
                "New consultation request from %s for %s case",
                clientName, caseType
        );
        logger.info("Lawyer notification: {}", message);
    }
}