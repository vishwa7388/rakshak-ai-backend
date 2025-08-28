package com.rakshakai.rakshak_backend.controller;

import com.rakshakai.rakshak_backend.dto.EmergencyCaseDto;
import com.rakshakai.rakshak_backend.model.EmergencyCase;
import com.rakshakai.rakshak_backend.repository.EmergencyCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/emergency")
public class EmergencyCaseController {

    @Autowired
    private EmergencyCaseRepository emergencyCaseRepository;

    @GetMapping("/cases/high-priority")
    public List<EmergencyCaseDto> getHighPriorityCases() {
        return emergencyCaseRepository.findHighPriorityOpenCases()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private EmergencyCaseDto convertToDto(EmergencyCase emergencyCase) {
        return new EmergencyCaseDto(
                emergencyCase.getId(),
                emergencyCase.getCaseNumber(),
                emergencyCase.getReporter() != null ? emergencyCase.getReporter().getName() : null,
                emergencyCase.getReporter() != null ? emergencyCase.getReporter().getEmail() : null,
                emergencyCase.getIncidentType(),
                emergencyCase.getDescription(),
                emergencyCase.getLocation(),
                emergencyCase.getLatitude(),
                emergencyCase.getLongitude(),
                emergencyCase.getStatus(),
                emergencyCase.getPriorityLevel(),
                emergencyCase.getCreatedAt(),
                emergencyCase.getAssignedOfficer()
        );
    }

    // Other methods remain same
}