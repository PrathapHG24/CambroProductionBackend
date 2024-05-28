package com.demo.tableController;

import com.demo.tableService.DynamicTableService;
import com.demo.tableService.ShowDatabaseService;
import com.demo.tableService.ShowTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.*;
@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class IdentifyController {

    @Autowired
    private ShowDatabaseService databaseService;

    @Autowired
    private ShowTableService tableService;

    @Autowired
    private DynamicTableService dynamicTableService;
//this code finds only one matching table from single database 
//     @PostMapping("/identify")
// public ResponseEntity<Object> identifyTableAndDatabase(@RequestBody Map<String, String> jsonInput) {
//     try {
//         String targetDatabase = "mold1"; // Specify the target database name here
//         List<Map<String, String>> tables = this.tableService.getAllTables(targetDatabase);
//         HashMap<String, Set<String>> tableColumns = new HashMap<>();
//         HashMap<String, String> tableDatabase = new HashMap<>();

//         tables.forEach(table -> {
//             String tableName = table.get("name");
//             List<Map<String, String>> columns = this.dynamicTableService.getTableColumns(targetDatabase, tableName);
//             Set<String> columnNames = columns.stream().map(column -> column.get("columnName")).collect(Collectors.toSet());
//             tableColumns.put(tableName, columnNames);
//             tableDatabase.put(tableName, targetDatabase);
//         });

//         Set<String> jsonColumnNames = jsonInput.keySet();

//         for (Map.Entry<String, Set<String>> entry : tableColumns.entrySet()) {
//             String tableName = entry.getKey();
//             Set<String> columnNames = entry.getValue();

//             if (columnNames.equals(jsonColumnNames)) {
//                 // Match found
//                 Map<String, String> result = new HashMap<>();
//                 result.put("tableName", tableName);
//                 result.put("databaseName", tableDatabase.get(tableName));
//                 return ResponseEntity.ok(result);
//             }
//         }

//         // No match found
//         Map<String, String> errorResponse = new HashMap<>();
//         errorResponse.put("error", "Table not found for the given JSON input in the specified database.");
//         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
//     } catch (Exception e) {
//         // Log the exception
//         e.printStackTrace();
//         // Return an appropriate response
//         Map<String, String> errorResponse = new HashMap<>();
//         errorResponse.put("error", "An error occurred while processing the request.");
//         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//     }
//     }
//}

// this code will find the all the matching tables from the single database
@PostMapping("/identify")
public ResponseEntity<Object> identifyTableAndDatabase(@RequestBody Map<String, String> jsonInput) {
    // Check if jsonInput is null or empty
    if (jsonInput == null || jsonInput.isEmpty()) {
        return ResponseEntity.badRequest().body("JSON input is null or empty");
    }
     // Add starttime, endtime, and date to the JSON input
    jsonInput.put("startTime", "2024-02-29 14:56:11");
    jsonInput.put("endTime", null); // Assuming endtime is initially null
    jsonInput.put("date", "2024-02-29 14:56:11");
System.out.println(jsonInput.toString());

    try {
        String targetDatabaseName = "mold1"; // Change this to your target database name
        List<Map<String, String>> tables = this.tableService.getAllTables(targetDatabaseName);
        HashMap<String, Set<String>> tableColumns = new HashMap<>();
        HashMap<String, String> tableDatabase = new HashMap<>();

        // Populate the map with table names and associated columns
        tables.forEach(table -> {
            String tableName = table.get("name");
            List<Map<String, String>> columns = this.dynamicTableService.getTableColumns(targetDatabaseName, tableName);
            Set<String> columnNames = columns.stream().map(column -> column.get("columnName")).collect(Collectors.toSet());
            tableColumns.put(tableName, columnNames);
            tableDatabase.put(tableName, targetDatabaseName);
        });   

        Set<String> jsonColumnNames = jsonInput.keySet();
        List<Map<String, String>> matchingTables = new ArrayList<>();

        for (Map.Entry<String, Set<String>> entry : tableColumns.entrySet()) {
            String tableName = entry.getKey();
            Set<String> columnNames = entry.getValue();

            if (columnNames.equals(jsonColumnNames)) {
                // Match found
                Map<String, String> result = new HashMap<>();
                result.put("tableName", tableName);
                result.put("databaseName", tableDatabase.get(tableName));
                matchingTables.add(result);
            }
        }

        if (!matchingTables.isEmpty()) {
            return ResponseEntity.ok(matchingTables);
        } else {
            // No match found
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "No matching table found for the given JSON input in the specified database.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    } catch (Exception e) {
        // Log the exception
        e.printStackTrace();
        // Return an appropriate response
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "An error occurred while processing the request.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
 }