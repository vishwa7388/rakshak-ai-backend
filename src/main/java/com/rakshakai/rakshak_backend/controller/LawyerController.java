package com.rakshakai.rakshak_backend.controller;

import com.rakshakai.rakshak_backend.dto.LawyerProfileDto;
import com.rakshakai.rakshak_backend.service.LawyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lawyers")
@CrossOrigin(origins = "*")
public class LawyerController {

    @Autowired
    private LawyerService lawyerService;

    // Register new lawyer
    @PostMapping("/register")
    public ResponseEntity<LawyerProfileDto> registerLawyer(
            @RequestBody LawyerProfileDto profileDto,
            @RequestParam Long userId) {
        try {
            LawyerProfileDto registeredLawyer = lawyerService.registerLawyer(profileDto, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredLawyer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Advanced search
    @GetMapping("/search")
    public ResponseEntity<List<LawyerProfileDto>> searchLawyers(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxFee,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(defaultValue = "false") Boolean verifiedOnly) {

        List<LawyerProfileDto> lawyers = lawyerService.searchLawyers(
                city, specialization, minRating, maxFee, minExperience, verifiedOnly
        );
        return ResponseEntity.ok(lawyers);
    }

    // Get top rated lawyers
    @GetMapping("/top-rated")
    public ResponseEntity<Page<LawyerProfileDto>> getTopRatedLawyers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<LawyerProfileDto> lawyers = lawyerService.getTopRatedLawyers(page, size);
        return ResponseEntity.ok(lawyers);
    }

    // Get recommended lawyers
    @GetMapping("/recommendations")
    public ResponseEntity<List<LawyerProfileDto>> getRecommendedLawyers(
            @RequestParam String caseType,
            @RequestParam String userCity) {
        List<LawyerProfileDto> lawyers = lawyerService.getRecommendedLawyers(caseType, userCity);
        return ResponseEntity.ok(lawyers);
    }

    // Verify lawyer (Admin endpoint)
    @PutMapping("/{lawyerId}/verify")
    public ResponseEntity<LawyerProfileDto> verifyLawyer(
            @PathVariable Long lawyerId,
            @RequestParam String verificationStatus) {
        try {
            LawyerProfileDto verifiedLawyer = lawyerService.verifyLawyer(lawyerId, verificationStatus);
            return ResponseEntity.ok(verifiedLawyer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Update availability
    @PutMapping("/{lawyerId}/availability")
    public ResponseEntity<LawyerProfileDto> updateAvailability(
            @PathVariable Long lawyerId,
            @RequestParam String status) {
        try {
            LawyerProfileDto updatedLawyer = lawyerService.updateAvailability(lawyerId, status);
            return ResponseEntity.ok(updatedLawyer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Get lawyer analytics
    @GetMapping("/{lawyerId}/analytics")
    public ResponseEntity<Map<String, Object>> getLawyerAnalytics(@PathVariable Long lawyerId) {
        try {
            Map<String, Object> analytics = lawyerService.getLawyerAnalytics(lawyerId);
            return ResponseEntity.ok(analytics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}