package com.rakshakai.rakshak_backend.controller;

import com.rakshakai.rakshak_backend.model.LawyerProfile;
import com.rakshakai.rakshak_backend.repository.LawyerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lawyers")
public class LawyerController {

    @Autowired
    private LawyerProfileRepository lawyerProfileRepository;

    @PostMapping("/register")
    public LawyerProfile registerLawyer(@RequestBody LawyerProfile lawyerProfile) {
        return lawyerProfileRepository.save(lawyerProfile);
    }

    @GetMapping("/search")
    public List<LawyerProfile> searchLawyers(
            @RequestParam String city,
            @RequestParam String specialization) {

        System.out.println("=== SEARCH DEBUG ===");
        System.out.println("Searching for: City=" + city + ", Spec=" + specialization);

        // Method 1: Get all from city first
        List<LawyerProfile> cityLawyers = lawyerProfileRepository.findByCity(city);
        System.out.println("Lawyers in " + city + ": " + cityLawyers.size());

        // Method 2: Filter by specialization
        List<LawyerProfile> filtered = cityLawyers.stream()
                .filter(lawyer -> lawyer.getSpecializations() != null &&
                        lawyer.getSpecializations().contains(specialization))
                .collect(Collectors.toList());

        System.out.println("After specialization filter: " + filtered.size());
        System.out.println("=== SEARCH END ===");

        return filtered;
    }

    @GetMapping("/top-rated")
    public List<LawyerProfile> getTopRatedLawyers(@RequestParam(defaultValue = "4.0") Double minRating) {
        return lawyerProfileRepository.findTopRatedLawyers(minRating);
    }

    @GetMapping("/verified")
    public List<LawyerProfile> getVerifiedLawyers() {
        return lawyerProfileRepository.findByIsVerified(true);
    }

    @PutMapping("/{lawyerId}/verify")
    public LawyerProfile verifyLawyer(@PathVariable Long lawyerId) {
        LawyerProfile lawyer = lawyerProfileRepository.findById(lawyerId).orElse(null);
        if (lawyer != null) {
            lawyer.setVerified(true);
            return lawyerProfileRepository.save(lawyer);
        }
        return null;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllLawyers() {
        List<LawyerProfile> lawyers = lawyerProfileRepository.findAll();
        System.out.println("=== ALL LAWYERS CHECK ===");
        System.out.println("Total lawyers: " + lawyers.size());

        if (lawyers.isEmpty()) {
            return ResponseEntity.ok("No lawyers registered yet!");
        }

        lawyers.forEach(lawyer -> {
            System.out.println("Lawyer ID: " + lawyer.getId());
            System.out.println("User: " + lawyer.getUser().getName());
            System.out.println("City: " + lawyer.getCity());
            System.out.println("Specializations: " + lawyer.getSpecializations());
            System.out.println("---");
        });

        return ResponseEntity.ok(lawyers);
    }
}