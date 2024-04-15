package com.demo.tableController;
import com.demo.tableService.ShowTableService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ShowTableController {

    @Autowired
    private ShowTableService tableService;

    @GetMapping("/database/{dbName}")
    @CrossOrigin("*")
    public List<Map<String, String>> getAllTables(@PathVariable("dbName") String databaseName) {
    
        return tableService.getAllTables(databaseName);
    }
}