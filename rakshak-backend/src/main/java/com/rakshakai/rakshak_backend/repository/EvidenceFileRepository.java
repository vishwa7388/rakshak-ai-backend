package com.rakshakai.rakshak_backend.repository;

import com.rakshakai.rakshak_backend.model.EvidenceFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvidenceFileRepository extends JpaRepository<EvidenceFile, Long> {
    List<EvidenceFile> findByEmergencyCaseId(Long caseId);
    List<EvidenceFile> findByFileType(String fileType);
    List<EvidenceFile> findByUploadedBy(Long userId);
    List<EvidenceFile> findByIsVerified(Boolean verified);
}
