package com.rakshakai.rakshak_backend.service;

import com.rakshakai.rakshak_backend.model.EvidenceFile;
import com.rakshakai.rakshak_backend.model.EmergencyCase;
import com.rakshakai.rakshak_backend.repository.EvidenceFileRepository;
import com.rakshakai.rakshak_backend.repository.EmergencyCaseRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class EvidenceService {

    @Autowired
    private EvidenceFileRepository evidenceFileRepository;

    @Autowired
    private EmergencyCaseRepository emergencyCaseRepository;

    @Autowired(required = false) // Make S3 optional for local testing
    private AmazonS3 amazonS3;

    @Value("${aws.s3.bucket:rakshak-ai-storage-2025}")
    private String s3BucketName;

    // Upload evidence file
    public EvidenceFile uploadEvidence(Long caseId, MultipartFile file,
                                       String uploadedBy, String description) throws IOException {

        // Validate case exists
        EmergencyCase emergencyCase = emergencyCaseRepository.findById(caseId)
                .orElseThrow(() -> new RuntimeException("Case not found with ID: " + caseId));

        // Generate unique S3 key
        String s3Key = generateS3Key(caseId, file.getOriginalFilename());

        // Upload to S3 (skip if S3 not configured for local testing)
        if(amazonS3 != null) {
            uploadToS3(s3Key, file);
        }

        // Save metadata to database
        EvidenceFile evidenceFile = new EvidenceFile();
        evidenceFile.setEmergencyCase(emergencyCase);
        evidenceFile.setFileName(file.getOriginalFilename());
        evidenceFile.setFileType(file.getContentType());
        evidenceFile.setS3Key(s3Key);
        evidenceFile.setS3Bucket(s3BucketName);
        evidenceFile.setFileSize(file.getSize());
        evidenceFile.setUploadTimestamp(LocalDateTime.now());
        evidenceFile.setUploadedBy(uploadedBy);
        evidenceFile.setDescription(description);
        evidenceFile.setVerified(false);

        return evidenceFileRepository.save(evidenceFile);
    }

    // Get all evidence for a case
    public List<EvidenceFile> getEvidenceForCase(Long caseId) {
        return evidenceFileRepository.findByEmergencyCaseId(caseId);
    }

    // Get pre-signed URL for file download
    public String getDownloadUrl(Long evidenceId) {
        EvidenceFile evidenceFile = evidenceFileRepository.findById(evidenceId)
                .orElseThrow(() -> new RuntimeException("Evidence not found"));

        if(amazonS3 != null) {
            // Generate pre-signed URL valid for 1 hour
            Date expiration = new Date(System.currentTimeMillis() + 3600000);
            return amazonS3.generatePresignedUrl(
                    evidenceFile.getS3Bucket(),
                    evidenceFile.getS3Key(),
                    expiration
            ).toString();
        }

        // For local testing without S3
        return "http://localhost:8080/mock-download/" + evidenceFile.getS3Key();
    }

    // Delete evidence
    public void deleteEvidence(Long evidenceId) {
        EvidenceFile evidenceFile = evidenceFileRepository.findById(evidenceId)
                .orElseThrow(() -> new RuntimeException("Evidence not found"));

        // Delete from S3
        if(amazonS3 != null) {
            amazonS3.deleteObject(new DeleteObjectRequest(
                    evidenceFile.getS3Bucket(),
                    evidenceFile.getS3Key()
            ));
        }

        // Delete from database
        evidenceFileRepository.delete(evidenceFile);
    }

    // Verify evidence (for police/admin)
    public EvidenceFile verifyEvidence(Long evidenceId, boolean isVerified) {
        EvidenceFile evidenceFile = evidenceFileRepository.findById(evidenceId)
                .orElseThrow(() -> new RuntimeException("Evidence not found"));

        evidenceFile.setVerified(isVerified);
        return evidenceFileRepository.save(evidenceFile);
    }

    // Get evidence statistics for a case
    public Map<String, Object> getEvidenceStats(Long caseId) {
        List<EvidenceFile> evidenceFiles = evidenceFileRepository.findByEmergencyCaseId(caseId);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalFiles", evidenceFiles.size());
        stats.put("verifiedFiles", evidenceFiles.stream().filter(EvidenceFile::getVerified).count());
        stats.put("totalSize", evidenceFiles.stream().mapToLong(EvidenceFile::getFileSize).sum());

        // Group by file type
        Map<String, Long> fileTypes = evidenceFiles.stream()
                .collect(Collectors.groupingBy(EvidenceFile::getFileType, Collectors.counting()));
        stats.put("fileTypes", fileTypes);

        return stats;
    }

    // Helper method to generate unique S3 key
    private String generateS3Key(Long caseId, String originalFilename) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String extension = "";
        if(originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return "cases/" + caseId + "/evidence_" + timestamp + extension;
    }

    // Upload file to S3
    private void uploadToS3(String s3Key, MultipartFile file) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        PutObjectRequest putRequest = new PutObjectRequest(
                s3BucketName,
                s3Key,
                file.getInputStream(),
                metadata
        );

        amazonS3.putObject(putRequest);
    }

    // Validate file before upload
    public boolean validateFile(MultipartFile file) {
        // Check file size (max 10MB)
        if(file.getSize() > 10 * 1024 * 1024) {
            throw new RuntimeException("File size exceeds 10MB limit");
        }

        // Check allowed file types
        List<String> allowedTypes = Arrays.asList(
                "image/jpeg", "image/png", "image/jpg",
                "video/mp4", "video/mpeg",
                "audio/mpeg", "audio/wav",
                "application/pdf"
        );

        if(!allowedTypes.contains(file.getContentType())) {
            throw new RuntimeException("File type not allowed: " + file.getContentType());
        }

        return true;
    }
}