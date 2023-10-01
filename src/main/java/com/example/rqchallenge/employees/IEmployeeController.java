package com.example.rqchallenge.employees;

import com.example.rqchallenge.employees.models.CreateEmployee;
import com.example.rqchallenge.employees.models.Employee;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
public interface IEmployeeController {
    @Operation(summary = "Get all employee records", description = "Returns all employee records")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message="Successfully retrieved")
    })
    @GetMapping()
    ResponseEntity<List<Employee>> getAllEmployees();

    @Operation(summary = "Get all employee records with name matching with searchString", description = "Returns all matched employee records")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message="Successfully retrieved")
    })
    @GetMapping("/search/{searchString}")
    ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable("searchString")  @NotBlank @Size(max = 512) String searchString);
    @Operation(summary = "Get a Employee by id", description = "Returns a Employee as per the id")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message="Successfully retrieved"),
            @ApiResponse(code = 404,message= "Not found - The Employee was not found")
    })
    @GetMapping("/{id}")
    ResponseEntity<Employee> getEmployeeById(@PathVariable("id")  @NotBlank @Size(max = 256) String id);

    @Operation(summary = "Get highest salary from all employees", description = "Returns highest salary from all employees")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message="Successfully retrieved")
    })
    @GetMapping("/highestSalary")
    ResponseEntity<Integer> getHighestSalaryOfEmployees();

    @Operation(summary = "Get top 10 highest employee names", description = "Returns names of top ten earning employees")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message="Successfully retrieved")
    })
    @GetMapping("/topTenHighestEarningEmployeeNames")
    ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames();

    @Operation(summary = "Create employee", description = "Returns created employee model")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message="Successfully retrieved"),
            @ApiResponse(code = 400,message="Constraint error")
    })
    @PostMapping()
    ResponseEntity<Employee> createEmployee(@RequestBody @Valid CreateEmployee createEmployee);

    @Operation(summary = "Delete a Employee by id", description = "Returns a Deleted employee name")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message="Successfully deleted"),
            @ApiResponse(code = 404,message= "Not found - The Employee was not found")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteEmployeeById(@PathVariable("id")  @NotBlank @Size(max = 256) String id);

}
