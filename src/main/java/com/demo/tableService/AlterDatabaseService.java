package com.demo.tableService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class AlterDatabaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void renameDatabase(String oldName, String newName) throws Exception {
        // Step 1: Create New Database
        jdbcTemplate.execute("CREATE DATABASE " + newName);

        // Step 2: Dump Old Database
        ProcessBuilder dumpProcessBuilder = new ProcessBuilder(
                "mysqldump", "-u", "username", "-p", "password", oldName);
        dumpProcessBuilder.redirectErrorStream(true);
        Process dumpProcess = dumpProcessBuilder.start();

        // Wait for the dump process to complete and check for errors
        if (dumpProcess.waitFor() != 0) {
            throw new RuntimeException("Error in dumping database");
        }

        // Step 3: Import Dump into New Database
        ProcessBuilder importProcessBuilder = new ProcessBuilder(
                "mysql", "-u", "username", "-p", "password", newName);
        importProcessBuilder.redirectErrorStream(true);
        Process importProcess = importProcessBuilder.start();

        BufferedReader br = new BufferedReader(new InputStreamReader(dumpProcess.getInputStream()));
        String line;
        while ((line = br.readLine()) != null) {
            importProcess.getOutputStream().write(line.getBytes());
        }
        importProcess.getOutputStream().close();

        // Wait for the import process to complete and check for errors
        if (importProcess.waitFor() != 0) {
            throw new RuntimeException("Error in importing database");
        }

        // Step 4: Drop Old Database
        jdbcTemplate.execute("DROP DATABASE " + oldName);
    }
}