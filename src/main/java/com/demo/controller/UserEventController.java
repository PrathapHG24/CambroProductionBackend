package com.demo.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.demo.service.UserEventService;

@RestController
@RequestMapping("/user-event")
@CrossOrigin("*")
public class UserEventController {

    @Autowired
    private UserEventService userEventService; // Inject UserEventService
   
    //login UserEvent is their oin AuuthenticateController.java class
   
    @PostMapping("/logout")
    @CrossOrigin("*")
    public ResponseEntity<String> logout() {
        try {
            userEventService.updateLogoutTime();
            return ResponseEntity.ok("Logout time updated successfully");
        } catch (DataAccessException e) {
            // Handle database access exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update logout time: " + e.getMessage());
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    
    @GetMapping("/batch-status")
@CrossOrigin("*")
public ResponseEntity<Map<String, Object>> getStartTimeEndTimeScheduleID() {
    try {
        Map<String, Object> result = userEventService.getStartTimeEndTimeScheduleID();
        if (result.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", result.get("error")));
        } else {
            Map<String, Object> response = new HashMap<>();
            boolean isBatchRunning = (Boolean) result.get("isBatchRunning");
            response.put("isBatchRunning", isBatchRunning);
            response.put("scheduleId", result.get("scheduleId"));
            return ResponseEntity.ok(response);
        }
    } catch (DataAccessException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Failed to fetch batch status and schedule ID by last login time: " + e.getMessage()));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "An error occurred: " + e.getMessage()));
    }
}
}
