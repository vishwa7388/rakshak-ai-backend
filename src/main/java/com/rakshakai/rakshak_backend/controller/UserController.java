package com.rakshakai.rakshak_backend.controller;

import com.rakshakai.rakshak_backend.model.User;
import com.rakshakai.rakshak_backend.repository.EmergencyCaseRepository;
import com.rakshakai.rakshak_backend.repository.EvidenceFileRepository;
import com.rakshakai.rakshak_backend.repository.LawyerProfileRepository;
import com.rakshakai.rakshak_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmergencyCaseRepository emergencyCaseRepository;

    @Autowired
    private LawyerProfileRepository lawyerRepository;

    @Autowired
    private EvidenceFileRepository evidenceFileRepository;

    @GetMapping("/test")
    public String testConnection() {
        return "Rakshakai Database Connection Working!";
    }

    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/api/debug/tables")
    public Map<String, Object> checkTables() {
        Map<String, Object> result = new HashMap<>();

        try {
            result.put("users_count", userRepository.count());
            result.put("emergency_cases_count", emergencyCaseRepository.count());
            result.put("lawyers_count", lawyerRepository.count());
            result.put("evidence_files_count", evidenceFileRepository.count());

            // Last user ID check
            List<User> users = userRepository.findAll();
            if (!users.isEmpty()) {
                result.put("last_user_id", users.get(users.size()-1).getId());
                result.put("last_user_name", users.get(users.size()-1).getName());
            }

        } catch (Exception e) {
            result.put("error", e.getMessage());
        }

        return result;
    }
}