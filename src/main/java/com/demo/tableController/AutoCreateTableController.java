package com.demo.tableController;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.tableService.AutoCreateTableService;



@RestController
@CrossOrigin("*")
public class AutoCreateTableController {

    @Autowired
    private AutoCreateTableService autotableService;

    @PostMapping("/mapping/createTable")
    @CrossOrigin("*")
    public ResponseEntity<Map<String, String>> createTable(@RequestParam("dbName") String dbName,
                                                           @RequestParam("tableName") String tableName,
                                                           @RequestBody String jsonString) {
        Map<String, String> response = new HashMap<>();
        
        String responseMessage = autotableService.createTable(dbName, tableName, jsonString);
        
        if (responseMessage.startsWith("Error")) {
            // If an error occurred, return a 500 Internal Server Error response
            response.put("status", "error");
            response.put("message", responseMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } else {
            // If table creation was successful, return a 200 OK response
            response.put("status", "success");
            response.put("message", responseMessage);
            return ResponseEntity.ok(response);
        }
    }
}

