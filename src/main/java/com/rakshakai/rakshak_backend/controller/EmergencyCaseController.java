package com.rakshakai.rakshak_backend.controller;

import com.rakshakai.rakshak_backend.dto.EmergencyCaseDto;
import com.rakshakai.rakshak_backend.service.EmergencyCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/emergency")
@CrossOrigin(origins = "*") // Configure properly in production
public class EmergencyCaseController {

    @Autowired
    private EmergencyCaseService emergencyCaseService;

    // Create new emergency case
    @PostMapping("/cases/create")
    public ResponseEntity<EmergencyCaseDto> createEmergencyCase(
            @RequestBody EmergencyCaseDto caseDto,
            @RequestParam Long reporterId) {
        try {
            EmergencyCaseDto createdCase = emergencyCaseService.createEmergencyCase(caseDto, reporterId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCase);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Get high priority cases
    @GetMapping("/cases/high-priority")
    public ResponseEntity<List<EmergencyCaseDto>> getHighPriorityCases() {
        List<EmergencyCaseDto> cases = emergencyCaseService.getHighPriorityCases();
        return ResponseEntity.ok(cases);
    }

    // Get cases by reporter
    @GetMapping("/cases/reporter/{reporterId}")
    public ResponseEntity<List<EmergencyCaseDto>> getCasesByReporter(@PathVariable Long reporterId) {
        List<EmergencyCaseDto> cases = emergencyCaseService.getCasesByReporter(reporterId);
        return ResponseEntity.ok(cases);
    }

    // Update case status
    @PutMapping("/cases/{caseId}/status")
    public ResponseEntity<EmergencyCaseDto> updateCaseStatus(
            @PathVariable Long caseId,
            @RequestParam String status,
            @RequestParam(required = false) String assignedOfficer) {
        try {
            EmergencyCaseDto updatedCase = emergencyCaseService.updateCaseStatus(caseId, status, assignedOfficer);
            return ResponseEntity.ok(updatedCase);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Get cases near location
    @GetMapping("/cases/nearby")
    public ResponseEntity<List<EmergencyCaseDto>> getCasesNearLocation(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "5.0") Double radiusKm) {
        List<EmergencyCaseDto> cases = emergencyCaseService.getCasesNearLocation(latitude, longitude, radiusKm);
        return ResponseEntity.ok(cases);
    }

    // Get dashboard statistics
    @GetMapping("/dashboard/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = emergencyCaseService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }
}
