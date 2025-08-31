package com.rakshakai.rakshak_backend.repository;

import com.rakshakai.rakshak_backend.model.EvidenceFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvidenceFileRepository extends JpaRepository<EvidenceFile, Long> {

    // Find evidence files by emergency case ID
    List<EvidenceFile> findByEmergencyCaseId(Long emergencyCaseId);

    // Find verified evidence files for a case
    List<EvidenceFile> findByEmergencyCaseIdAndIsVerified(Long emergencyCaseId, boolean isVerified);

    // Count evidence files for a case
    Long countByEmergencyCaseId(Long emergencyCaseId);

    // Find evidence by uploader
    List<EvidenceFile> findByUploadedBy(String uploadedBy);

    // Get total storage used
    @Query("SELECT SUM(e.fileSize) FROM EvidenceFile e")
    Long getTotalStorageUsed();

    // Find evidence files by type
    List<EvidenceFile> findByFileType(String fileType);

    // Delete evidence files by case ID
    void deleteByEmergencyCaseId(Long emergencyCaseId);
}
