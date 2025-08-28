package com.rakshakai.rakshak_backend.dto;

import java.time.LocalDateTime;

public class EmergencyCaseDto {
    private Long id;
    private String caseNumber;
    private String reporterName;
    private String reporterEmail;
    private String incidentType;
    private String description;
    private String location;
    private Double latitude;
    private Double longitude;
    private String status;
    private String priorityLevel;
    private LocalDateTime createdAt;
    private String assignedOfficer;

    // Constructors
    public EmergencyCaseDto() {}

    public EmergencyCaseDto(Long id, String caseNumber, String reporterName,
                            String reporterEmail, String incidentType, String description,
                            String location, Double latitude, Double longitude,
                            String status, String priorityLevel, LocalDateTime createdAt,
                            String assignedOfficer) {
        this.id = id;
        this.caseNumber = caseNumber;
        this.reporterName = reporterName;
        this.reporterEmail = reporterEmail;
        this.incidentType = incidentType;
        this.description = description;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.priorityLevel = priorityLevel;
        this.createdAt = createdAt;
        this.assignedOfficer = assignedOfficer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getReporterEmail() {
        return reporterEmail;
    }

    public void setReporterEmail(String reporterEmail) {
        this.reporterEmail = reporterEmail;
    }

    public String getIncidentType() {
        return incidentType;
    }

    public void setIncidentType(String incidentType) {
        this.incidentType = incidentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(String priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getAssignedOfficer() {
        return assignedOfficer;
    }

    public void setAssignedOfficer(String assignedOfficer) {
        this.assignedOfficer = assignedOfficer;
    }
}