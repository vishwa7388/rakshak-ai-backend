package com.rakshakai.rakshak_backend.dto;

import java.time.LocalDateTime;

public class EvidenceFileDto {

    private Long id;
    private Long emergencyCaseId;
    private String caseNumber;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private String uploadedBy;
    private LocalDateTime uploadTimestamp;
    private String description;
    private boolean isVerified;
    private String downloadUrl;

    // Default constructor
    public EvidenceFileDto() {}

    // Constructor with all fields
    public EvidenceFileDto(Long id, Long emergencyCaseId, String caseNumber,
                           String fileName, String fileType, Long fileSize,
                           String uploadedBy, LocalDateTime uploadTimestamp,
                           String description, boolean isVerified, String downloadUrl) {
        this.id = id;
        this.emergencyCaseId = emergencyCaseId;
        this.caseNumber = caseNumber;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.uploadedBy = uploadedBy;
        this.uploadTimestamp = uploadTimestamp;
        this.description = description;
        this.isVerified = isVerified;
        this.downloadUrl = downloadUrl;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmergencyCaseId() {
        return emergencyCaseId;
    }

    public void setEmergencyCaseId(Long emergencyCaseId) {
        this.emergencyCaseId = emergencyCaseId;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
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

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public LocalDateTime getUploadTimestamp() {
        return uploadTimestamp;
    }

    public void setUploadTimestamp(LocalDateTime uploadTimestamp) {
        this.uploadTimestamp = uploadTimestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}

