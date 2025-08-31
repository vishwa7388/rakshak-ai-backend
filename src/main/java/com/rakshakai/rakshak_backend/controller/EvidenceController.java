package com.rakshakai.rakshak_backend.controller;

import com.rakshakai.rakshak_backend.model.EvidenceFile;
import com.rakshakai.rakshak_backend.service.EvidenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/evidence")
@CrossOrigin(origins = "*")
public class EvidenceController {

    @Autowired
    private EvidenceService evidenceService;

    // Upload evidence
    @PostMapping("/upload")
    public ResponseEntity<?> uploadEvidence(
            @RequestParam("file") MultipartFile file,
            @RequestParam Long caseId,
            @RequestParam String uploadedBy,
            @RequestParam(required = false) String description) {
        try {
            // Validate file
            evidenceService.validateFile(file);

            // Upload file
            EvidenceFile evidence = evidenceService.uploadEvidence(caseId, file, uploadedBy, description);
            return ResponseEntity.status(HttpStatus.CREATED).body(evidence);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Get evidence for a case
    @GetMapping("/case/{caseId}")
    public ResponseEntity<List<EvidenceFile>> getEvidenceForCase(@PathVariable Long caseId) {
        List<EvidenceFile> evidenceFiles = evidenceService.getEvidenceForCase(caseId);
        return ResponseEntity.ok(evidenceFiles);
    }

    // Get download URL
    @GetMapping("/{evidenceId}/download")
    public ResponseEntity<?> getDownloadUrl(@PathVariable Long evidenceId) {
        try {
            String downloadUrl = evidenceService.getDownloadUrl(evidenceId);
            return ResponseEntity.ok(Map.of("downloadUrl", downloadUrl));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Delete evidence
    @DeleteMapping("/{evidenceId}")
    public ResponseEntity<?> deleteEvidence(@PathVariable Long evidenceId) {
        try {
            evidenceService.deleteEvidence(evidenceId);
            return ResponseEntity.ok("Evidence deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Verify evidence (Admin/Police endpoint)
    @PutMapping("/{evidenceId}/verify")
    public ResponseEntity<EvidenceFile> verifyEvidence(
            @PathVariable Long evidenceId,
            @RequestParam boolean isVerified) {
        try {
            EvidenceFile verifiedEvidence = evidenceService.verifyEvidence(evidenceId, isVerified);
            return ResponseEntity.ok(verifiedEvidence);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Get evidence statistics
    @GetMapping("/case/{caseId}/stats")
    public ResponseEntity<Map<String, Object>> getEvidenceStats(@PathVariable Long caseId) {
        Map<String, Object> stats = evidenceService.getEvidenceStats(caseId);
        return ResponseEntity.ok(stats);
    }
}