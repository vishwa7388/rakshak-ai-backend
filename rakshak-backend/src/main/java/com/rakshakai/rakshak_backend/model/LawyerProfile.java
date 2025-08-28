package com.rakshakai.rakshak_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "lawyer_profiles")
public class LawyerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user; // Links to User with userType = LAWYER

    @Column(name = "bar_council_number", unique = true)
    private String barCouncilNumber;

    @Column(name = "specializations")
    private String specializations; // CRIMINAL, CIVIL, FAMILY, CORPORATE, etc.

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "consultation_fee")
    private Double consultationFee;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "total_cases")
    private Integer totalCases;

    @Column(name = "success_rate")
    private Double successRate;

    @Column(name = "office_address")
    private String officeAddress;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "languages")
    private String languages; // HINDI, ENGLISH, BENGALI, etc.

    @Column(name = "availability_status")
    private String availabilityStatus; // AVAILABLE, BUSY, OFFLINE

    @Column(name = "is_verified")
    private Boolean isVerified;

    @Column(name = "verification_date")
    private LocalDateTime verificationDate;

    @Column(name = "profile_created_at")
    private LocalDateTime profileCreatedAt;

    // Constructor
    public LawyerProfile() {
        this.profileCreatedAt = LocalDateTime.now();
        this.isVerified = false;
        this.rating = 0.0;
        this.totalCases = 0;
        this.availabilityStatus = "AVAILABLE";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBarCouncilNumber() {
        return barCouncilNumber;
    }

    public void setBarCouncilNumber(String barCouncilNumber) {
        this.barCouncilNumber = barCouncilNumber;
    }

    public String getSpecializations() {
        return specializations;
    }

    public void setSpecializations(String specializations) {
        this.specializations = specializations;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public Double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(Double consultationFee) {
        this.consultationFee = consultationFee;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getTotalCases() {
        return totalCases;
    }

    public void setTotalCases(Integer totalCases) {
        this.totalCases = totalCases;
    }

    public Double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(Double successRate) {
        this.successRate = successRate;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public LocalDateTime getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(LocalDateTime verificationDate) {
        this.verificationDate = verificationDate;
    }

    public LocalDateTime getProfileCreatedAt() {
        return profileCreatedAt;
    }

    public void setProfileCreatedAt(LocalDateTime profileCreatedAt) {
        this.profileCreatedAt = profileCreatedAt;
    }

    // LawyerProfile.java में add करो:
    public boolean hasSpecialization(String spec) {
        if (this.specializations == null) return false;
        String[] specs = this.specializations.split(",");
        for (String s : specs) {
            if (s.trim().equalsIgnoreCase(spec.trim())) {
                return true;
            }
        }
        return false;
    }

    public boolean practicesInCity(String cityName) {
        return this.city != null &&
                this.city.equalsIgnoreCase(cityName);
    }
}