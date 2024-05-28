package com.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.model.UserEvent;
import com.demo.repository.UserEventRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserEventService {
   
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserEventRepository userEventRepository;

    public void saveLoginEvent(String username) throws DataAccessException {
        try {
            UserEvent userEvent = new UserEvent(username);
            userEvent.setLoginTime(LocalDateTime.now()); // Set login time
            userEventRepository.save(userEvent);
        } catch (DataAccessException e) {
            // Handle database access exception
            throw new DataAccessException("Failed to save login event", e) {};
        }
    }

    public void updateWithScheduleId(String scheduleId) throws DataAccessException {
        try {
            UserEvent userEvent = userEventRepository.findFirstByOrderByLoginTimeDesc();
            if (userEvent != null) {
                userEvent.setScheduleId(scheduleId);
                userEvent.setScheduleIdTime(LocalDateTime.now());
                userEventRepository.save(userEvent);
            } else {
                // Handle user event not found
                throw new DataAccessException("User event not found for username") {};
            }
        } catch (DataAccessException e) {
            // Handle database access exception
            throw new DataAccessException("Failed to update with schedule ID", e) {};
        }
    }
       //to update the BatchEndTime In userEvent table
    @Transactional
    public void updateBatchEndTime() throws DataAccessException {
        try {
            // Retrieve the last UserEvent from the database based on login time
            UserEvent latestUserEvent = userEventRepository.findFirstByOrderByLoginTimeDesc();
            if (latestUserEvent != null) {
                // Set BatchEndTime based on the latest LoginTime
                latestUserEvent.setBatchEndTime(LocalDateTime.now());
                
                // Save the updated UserEvent
                userEventRepository.save(latestUserEvent);
            } else {
                // Handle case where no UserEvent is found
                throw new DataAccessResourceFailureException("No UserEvent found in the database");
            }
        } catch (DataAccessException e) {
            // Handle database access exception
            throw new DataAccessResourceFailureException("Failed to update BatchEndTime", e);
        }
    }


    //to update the logoutTimr in UserEvent Table
   public void updateLogoutTime() throws DataAccessException {
        try {
            UserEvent userEvent = userEventRepository.findFirstByOrderByLoginTimeDesc();
            if (userEvent != null) {
                // Only update logout time if it's not already set
                if (userEvent.getLogoutTime() == null) {
                    userEvent.setLogoutTime(LocalDateTime.now());
                    userEventRepository.save(userEvent);
                }
            } else {
                // Handle user event not found
                throw new DataAccessResourceFailureException("User event not found for username ");
            }
        } catch (DataAccessException e) {
            // Handle database access exception
            throw new DataAccessResourceFailureException("Failed to update logout time", e);
        }
    }
   
    // You can add more methods here as needed
   // Method to get the start time, end time, and schedule ID
   public Map<String, Object> getStartTimeEndTimeScheduleID() {
    Map<String, Object> result = new HashMap<>();

    try {
        String sqlQuery = "SELECT CASE " +
                                "WHEN endTime IS NULL THEN 1 " +
                                "ELSE 0 " +
                           "END AS isBatchRunning, " +
                           "CASE WHEN endTime IS NULL THEN Schedule_Olsn_Release_Barcode ELSE NULL END AS scheduleId " +
                           "FROM w701 " +
                           "ORDER BY startTime DESC " +
                           "LIMIT 1";

        result = jdbcTemplate.queryForMap(sqlQuery);
        // Convert the isBatchRunning value to boolean
        Long isBatchRunningValue = (Long) result.get("isBatchRunning");
        boolean isBatchRunning = (isBatchRunningValue == 1) ? true : false;
        result.put("isBatchRunning", isBatchRunning);
    } catch (Exception e) {
        e.printStackTrace();
        result.put("error", "Unable to fetch batch status");
    }

    return result;
}

}