/*
package com.rakshakai.rakshak_backend.config;

import com.rakshakai.rakshak_backend.model.*;
import com.rakshakai.rakshak_backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class Datainitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LawyerProfileRepository lawyerProfileRepository;

    @Autowired
    private EmergencyCaseRepository emergencyCaseRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        if (userRepository.count() > 0) {
            System.out.println("=== Data already exists, skipping initialization ===");
            return;
        }

        System.out.println("=== Initializing Sample Data for Rakshakai ===");

        // Create sample users
        User citizen1 = createUser("Rahul Sharma", "rahul@example.com", "CITIZEN");
        User citizen2 = createUser("Priya Singh", "priya@example.com", "CITIZEN");
        User lawyer1User = createUser("Adv. Amit Verma", "amit.verma@lawfirm.com", "LAWYER");
        User lawyer2User = createUser("Adv. Neha Gupta", "neha.gupta@legal.com", "LAWYER");
        User policeUser = createUser("Inspector Raj Kumar", "raj.kumar@police.gov.in", "POLICE");

        // Create lawyer profiles
        LawyerProfile lawyer1 = createLawyerProfile(lawyer1User,
                "UP/2015/12345",
                Arrays.asList("Criminal Law", "Civil Law"),
                10, 5000.0, "Meerut");

        LawyerProfile lawyer2 = createLawyerProfile(lawyer2User,
                "UP/2018/67890",
                Arrays.asList("Family Law", "Property Law", "Criminal Law"),
                7, 3500.0, "Meerut");

        // Create sample emergency cases
        EmergencyCase case1 = createEmergencyCase(citizen1,
                "THEFT",
                "Mobile phone stolen near Sadar Bazar",
                "Sadar Bazar, Meerut",
                28.9845, 77.7064);

        EmergencyCase case2 = createEmergencyCase(citizen2,
                "ASSAULT",
                "Road rage incident on Delhi Road",
                "Delhi Road, Meerut",
                28.9931, 77.7151);

        System.out.println("=== Sample Data Initialization Complete ===");
        System.out.println("Created:");
        System.out.println("- 5 Users (2 Citizens, 2 Lawyers, 1 Police)");
        System.out.println("- 2 Lawyer Profiles");
        System.out.println("- 2 Emergency Cases");
        System.out.println("\n=== You can now test the APIs ===");
        System.out.println("Test endpoints:");
        System.out.println("1. GET http://localhost:8080/api/users/all");
        System.out.println("2. GET http://localhost:8080/api/lawyers/all");
        System.out.println("3. GET http://localhost:8080/api/emergency/cases/high-priority");
        System.out.println("4. GET http://localhost:8080/api/lawyers/search?city=Meerut");
    }

    private User createUser(String name, String email, String userType) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setUserType(userType);
        return userRepository.save(user);
    }

    private LawyerProfile createLawyerProfile(User user, String barNumber,
                                              List<String> specializations,
                                              int experience, double fee, String city) {
        LawyerProfile profile = new LawyerProfile();
        profile.setUser(user);
        profile.setBarCouncilNumber(barNumber);
        profile.setSpecializations(specializations);
        profile.setExperienceYears(experience);
        profile.setConsultationFee(fee);
        profile.setCity(city);
        profile.setState("Uttar Pradesh");
        profile.setLanguages(Arrays.asList("Hindi", "English"));
        profile.setOfficeAddress("Court Complex, " + city);
        profile.setRating(4.5);
        profile.setTotalCases(50);
        profile.setSuccessRate(75.0);
        profile.setAvailabilityStatus("AVaiLABLE");
        profile.setVerified(true);
        return lawyerProfileRepository.save(profile);
    }

    private EmergencyCase createEmergencyCase(User reporter, String incidentType,
                                              String description, String location,
                                              double latitude, double longitude) {
        EmergencyCase emergencyCase = new EmergencyCase();
        emergencyCase.setCaseNumber("CASE" + System.currentTimeMillis());
        emergencyCase.setReporter(reporter);
        emergencyCase.setIncidentType(incidentType);
        emergencyCase.setDescription(description);
        emergencyCase.setLocation(location);
        emergencyCase.setLatitude(latitude);
        emergencyCase.setLongitude(longitude);
        emergencyCase.setStatus("OPEN");
        emergencyCase.setPriorityLevel(incidentType.equals("ASSAULT") ? "HIGH" : "MEDIUM");
        emergencyCase.setCreatedAt(LocalDateTime.now());
        return emergencyCaseRepository.save(emergencyCase);
    }
}*/
