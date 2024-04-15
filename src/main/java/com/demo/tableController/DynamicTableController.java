package com.demo.tableController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.demo.tableService.DynamicTableService;
import com.demo.util.ApiResponse;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class DynamicTableController {

    private static final Logger logger = LoggerFactory.getLogger(DynamicTableController.class);

    @Autowired
    private DynamicTableService dynamicTableService;
    
    // Delete Database feature
    @DeleteMapping("/dropDatabase/{dbName}")
    @CrossOrigin("*")
    public ResponseEntity<ApiResponse<String>> dropDatabase(@PathVariable String dbName) {
        
        try {
            String result = dynamicTableService.dropDatabase(dbName);
            ApiResponse<String> response = new ApiResponse<>(true, result, null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>(false, null, "Error dropping database: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    //delete table feature
    @DeleteMapping("/dropTable/dbName={dbName}/tableName={tableName}")
    @CrossOrigin("*")
    public ResponseEntity<ApiResponse<String>> dropTable(@PathVariable String dbName, @PathVariable String tableName) {
                try {
            String result = dynamicTableService.dropTable(dbName, tableName);
            ApiResponse<String> response = new ApiResponse<>(true, result, null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>(false, null, "Error dropping table: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
            // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error dropping table: " + e.getMessage());
        }
    }  
}



