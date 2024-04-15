package com.demo.tableController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.demo.tableService.VariableAndTagService;
import com.demo.util.ApiResponse;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
    

    @RequestMapping("/api")
    @CrossOrigin("*")
    @RestController
    public class VariableAndTagController {
    
        @Autowired
        private VariableAndTagService variableAndTagService;
    
        @PostMapping("/saveJsonKeyAsvalueAndTag")
        @CrossOrigin("*")
        public ResponseEntity<ApiResponse<String>> saveJsonKeyVariable(@RequestBody List<Map<String, Object>> dataList) {
                try {
                // Call the insertData method from MappingService
                variableAndTagService.saveJsonKeyVariables(dataList);
                // Construct and return the response
                ApiResponse<String> response = new ApiResponse<>(true, "Json Variable Name inserted successfully", null);
                return ResponseEntity.ok(response);
            } catch (SQLException e) {
                ApiResponse<String> response = new ApiResponse<>(false, null, e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }
       
        //to fetch the jsonVariable and plcTag name
       @GetMapping("/saveJsonKeyAsvalueAndTag")
       @CrossOrigin("*")
       public ResponseEntity<List<Map<String, Object>>> getDataFromTable() {
           try {
               List<Map<String, Object>> data = variableAndTagService.getAllDataFromTable();
               return ResponseEntity.ok(data);
           } catch (SQLException e) {
               e.printStackTrace();
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
           }
       }

        //this is to upadate the plcTag name
        @PostMapping("/updatePlcTag")
        @CrossOrigin("*")
        public ResponseEntity<ApiResponse<String>> updatePlcTag(@RequestParam("jsonVariable") String jsonVariable,
                                                   @RequestParam("newPlcTag") String newPlcTag) {
            try {
                variableAndTagService.updatePlcTagBasedOnJsonVariable(jsonVariable, newPlcTag);
                ApiResponse<String> response = new ApiResponse<>(true, "plcTag Name inserted successfully",null);
                return ResponseEntity.ok(response);
            } catch (SQLException e) {
                e.printStackTrace();
                ApiResponse<String> response = new ApiResponse<>(false, null, e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
           }
       }
      
       @PostMapping("/insertDataToPlc")
       @CrossOrigin("*")
       public void insertDatatoPlc(@RequestBody List<Map<String, Object>> dataList){

        for(Map<String, Object> entry : dataList){
           
            System.out.println(entry);
            
        }
        

    }
}