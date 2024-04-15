package com.demo.tableController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.tableDto.CreateDatabaseDto;
import com.demo.tableService.CreateDatabaseService;
import com.demo.util.ApiResponse;

@RestController
@CrossOrigin("*")
@RequestMapping("/database")

public class CreateDatabaseController {
   
    @Autowired
    private CreateDatabaseService createDatabaseService;

    @PostMapping("/savedatabase")
    @CrossOrigin("*")
    public ResponseEntity<ApiResponse<String>> createDatabase(@RequestBody CreateDatabaseDto databaseDTO) {
        try {
           String result = createDatabaseService.createDatabase(databaseDTO.getDbName());
           ApiResponse<String> response = new ApiResponse<>(true, result, null);
           return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>(false, null, "Error creating database: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
        
    }



     
}