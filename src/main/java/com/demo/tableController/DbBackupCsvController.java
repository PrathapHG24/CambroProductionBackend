package com.demo.tableController;
import com.demo.tableService.DbBackUpCsvService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class DbBackupCsvController {
    @Autowired    
    private DbBackUpCsvService dbBackUpCsvService;

    // take the back up of the db in the form of csv file.
        @GetMapping("/takebackup/table/{tableName}")
        @CrossOrigin("*")
        public ResponseEntity<Map<String, String>> exportDataAsCsv(@PathVariable String tableName) {
        try {
            // Attempt to export data to CSV
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String formattedDateTime = now.format(formatter);
            String outputFile = "D:\\TableBackup\\BackupData" + tableName + "_" + formattedDateTime + ".csv";
            dbBackUpCsvService.exportDataAsCsv(tableName, outputFile);

            // If successful, return success message with file path
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "'"+ tableName +"' Data exported successfully");
            response.put("filePath", outputFile);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // If an error occurs, return error message
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Table '" + tableName + "' does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}