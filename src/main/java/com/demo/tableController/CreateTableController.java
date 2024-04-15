package com.demo.tableController;

import com.demo.tableDto.CreateTableDto;
import com.demo.tableService.CreateTableService;
import com.demo.util.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/{dbName}/table")
public class CreateTableController {

    @Autowired
    private CreateTableService createTableService;

    @PostMapping("/create")
    @CrossOrigin("*")
    public ResponseEntity<ApiResponse<String>> createTable(@PathVariable("dbName") String dbName,
                                                          @RequestBody CreateTableDto tableRequest) {

                                                     
        try {
            String result = createTableService.createTable(dbName, tableRequest);
            ApiResponse<String> response = new ApiResponse<>(true, result, null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>(false, null, "Error creating table: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}