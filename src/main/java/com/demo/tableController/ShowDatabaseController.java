package com.demo.tableController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.tableService.ShowDatabaseService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ShowDatabaseController {

    @Autowired
    private ShowDatabaseService databaseService;

    @GetMapping("/showdbs")
    @CrossOrigin("*")
    public List<Map<String, String>> getAllDatabases() {
        return databaseService.getAllDatabases();
    }

}