package com.rakshakai.rakshak_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evidence_files")
public class EvidenceFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emergency_case_id")
    private EmergencyCase emergencyCase;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType; // IMAGE, AUDIO, VIDEO, DOCUMENT

    @Column(name = "s3_key")
    private String s3Key; // S3 object key for retrieval

    @Column(name = "s3_bucket")
    private String s3Bucket;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "upload_timestamp")
    private LocalDateTime uploadTimestamp;

    @Column(name = "uploaded_by")
    private Long uploadedBy; // User ID who uploaded

    @Column(name = "description")
    private String description;

    @Column(name = "is_verified")
    private Boolean isVerified; // Police verification status

    // Constructors
    public EvidenceFile() {
        this.uploadTimestamp = LocalDateTime.now();
        this.isVerified = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmergencyCase getEmergencyCase() {
        return emergencyCase;
    }

    public void setEmergencyCase(EmergencyCase emergencyCase) {
        this.emergencyCase = emergencyCase;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getS3Key() {
        return s3Key;
    }

    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public String getS3Bucket() {
        return s3Bucket;
    }

    public void setS3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public LocalDateTime getUploadTimestamp() {
        return uploadTimestamp;
    }

    public void setUploadTimestamp(LocalDateTime uploadTimestamp) {
        this.uploadTimestamp = uploadTimestamp;
    }

    public Long getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(Long uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }
}