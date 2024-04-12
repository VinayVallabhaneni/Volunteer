package com.project.volunteerOrganization.controller;

import com.project.volunteerOrganization.model.Organization;
import com.project.volunteerOrganization.model.Volunteer;
import com.project.volunteerOrganization.repository.IOrganizationRepository;
import com.project.volunteerOrganization.repository.IVolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@CrossOrigin("*")
public class AuthenticationController {

    @Autowired
    private IVolunteerRepository volunteerRepository;

    @Autowired
    private IOrganizationRepository organizationRepository;

    @PostMapping("/adminLogin")
    private ResponseEntity<?> adminLogin(@RequestBody HashMap<String, String> login) {
        try {
            String email = login.get("email");
            String password = login.get("password");

            if (!isValidCredentials(email, password, "admin@gmail.com", "admin")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
            }

            return ResponseEntity.ok("Admin login successful");
        } catch (Exception e) {
            return handleException(e, "An error occurred during admin login");
        }
    }

    @PostMapping("/volunteer/Login")
    private ResponseEntity<?> volunteerLogin(@RequestBody HashMap<String, String> login) {
        try {
            String email = login.get("email");
            String password = login.get("password");

            Volunteer volunteer = volunteerRepository.findByEmailAndPassword(email, password);
            if (volunteer == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
            }

            return ResponseEntity.ok("Volunteer login successful");
        } catch (Exception e) {
            return handleException(e, "An error occurred during volunteer login");
        }
    }

    @PostMapping("/organization/get/login")
    private ResponseEntity<?> organizationLogin(@RequestBody HashMap<String, String> login) {
        try {
            String email = login.get("email");
            String password = login.get("password");

            Organization organization = organizationRepository.findByEmailAndPassword(email, password);
            if (organization == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
            }

            return ResponseEntity.ok("Organization login successful");
        } catch (Exception e) {
            return handleException(e, "An error occurred during organization login");
        }
    }

    private boolean isValidCredentials(String providedEmail, String providedPassword, String expectedEmail, String expectedPassword) {
        return providedEmail != null && providedPassword != null
                && providedEmail.equals(expectedEmail)
                && providedPassword.equals(expectedPassword);
    }

    private ResponseEntity<?> handleException(Exception e, String message) {
        HashMap<String, Object> res = new HashMap<>();
        res.put("success", false);
        res.put("msg", message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
}

