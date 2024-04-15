package com.demo.tableController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.demo.tableService.AlterDatabaseService;

@RestController
@RequestMapping("/api/database")
@CrossOrigin("*")
public class AlterDatabaseController {

    @Autowired
    private AlterDatabaseService databaseService;

    @PutMapping("/rename")
    @CrossOrigin("*")
    public ResponseEntity<?> renameDatabase(@RequestParam String oldName, 
                                            @RequestParam String newName) {
        try {
            databaseService.renameDatabase(oldName, newName);
            return ResponseEntity.ok("Database renamed successfully from " + oldName + " to " + newName);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error renaming database: " + e.getMessage());
        }
    }
}