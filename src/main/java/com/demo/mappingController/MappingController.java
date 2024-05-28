package com.demo.mappingController;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.mappingService.MappingService;
import com.demo.service.UserEventService;
import com.demo.util.ApiRespons;
import com.demo.util.ApiResponse;
import com.demo.util.ApiResponse1;

@RestController
@RequestMapping("/mapping")
public class MappingController {

     @Autowired
    private UserEventService userEventService;

    @Autowired
    private MappingService mappingService;
    private static final Logger logger = LoggerFactory.getLogger(MappingController.class);


    @CrossOrigin("*")
    @PostMapping("/insertData")
    public ResponseEntity<ApiResponse1> insertData(@RequestParam String dbName,
                                                   @RequestParam String tableName,
                                                   @RequestBody List<Map<String, Object>> data) {
        try {
            if (data != null && !data.isEmpty()) {
                List<Map<String, Object>> insertedData = mappingService.insertData(dbName, tableName, data);
                return ResponseEntity.ok(ApiResponse1.success("Data inserted successfully", insertedData));
            } else {
                System.out.println("No data available to process.");
                return ResponseEntity.ok(ApiResponse1.success("No data available to process", null));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse1.error(e.getMessage()));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse1.error("SQL error occurred table not matched"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse1.error("Internal server error table not matched"));
        }
    }
    @CrossOrigin("*")
    @PostMapping("/updateEndTime")
    public ResponseEntity<ApiRespons> updateEndTime(@RequestParam String dbName, @RequestParam String tableName, @RequestParam String scheduleID) {
        try {
            userEventService.updateBatchEndTime();
            String result = mappingService.updateEndTime(dbName, tableName, scheduleID);
            ApiRespons response = new ApiRespons(result, null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to update end time", e);
            ApiRespons response = new ApiRespons(null, "Failed to update end time");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    //to save the schedule id
    @CrossOrigin("*")
    @PostMapping("/saveSchedulerId")
    public ResponseEntity<ApiResponse<String>> saveScheduleID(@RequestParam String scheduleID) {
       
        try {
            // Assuming that your dataService.saveScheduleID may throw exceptions
            String result=mappingService.saveScheduleID(scheduleID);
            ApiResponse<String> response = new ApiResponse<>(true, result, null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>(false, null, e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }  
    //to show the all schedule id                                                                    
     @CrossOrigin("*")
     @GetMapping("/getAllScheduleId")
    public List<Map<String, String>> getAllScheduleIDs() {
       
        return mappingService.getAllScheduleIDs();
    }
}
