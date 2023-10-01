package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.exceptions.EmployeeCreationException;
import com.example.rqchallenge.employees.exceptions.EmployeeNotFoundException;
import com.example.rqchallenge.employees.exceptions.NoDataFoundException;
import com.example.rqchallenge.employees.models.CreateEmployee;
import com.example.rqchallenge.employees.models.CreateEmployeesResponse;
import com.example.rqchallenge.employees.models.Employee;
import com.example.rqchallenge.employees.models.GetEmployeesResponse;
import com.example.rqchallenge.employees.utils.RestTemplateExecutor;
import com.example.rqchallenge.employees.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@EnableCaching
public class EmployeeService implements IEmployeeService {

    private final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    private RestTemplateExecutor restTemplateExecutor;

    @Autowired
    public EmployeeService(RestTemplateExecutor restTemplateExecutor) {
        this.restTemplateExecutor = restTemplateExecutor;
    }

    //NOTE:Caching result as downstream service is giving 429 frequently
    @Cacheable("employees")
    @Override
    public List<Employee> getAllEmployees() {
        Optional<String> response = restTemplateExecutor.execute("employees", HttpMethod.GET, null, String.class);
        if (response.isEmpty()) {
            return List.of();
        }
        return getEmployeesData(response.get());
    }

    private List<Employee> getEmployeesData(String responseData) {
        GetEmployeesResponse response = null;
        try {
            response = Utils.getObjectMapper().readValue(responseData, GetEmployeesResponse.class);
        } catch (JsonProcessingException e) {
            logger.error("Error while parsing Json: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return response.getData();
    }

    @Override
    public List<Employee> getEmployeesByNameSearch(String searchString) {
        List<Employee> employeeList = getAllEmployees();
        List<Employee> filteredList = employeeList.stream().parallel()
                .filter(employee -> employee.getName().toLowerCase().contains(searchString.toLowerCase()))
                .collect(Collectors.toList());
        return filteredList;
    }

    @Override
    public Employee getEmployeeById(String id) {
        List<Employee> employeeList = getAllEmployees();
        Employee filteredEmployee = employeeList.stream().parallel()
                .filter(employee -> employee.getId().equals(id))
                .findAny().orElseThrow(() -> new EmployeeNotFoundException(id));
        return filteredEmployee;
    }

    @Override
    public Integer getHighestSalaryOfEmployees() {
        List<Employee> employeeList = getAllEmployees();
        Optional<Employee> filteredEmployee = employeeList.stream().max(Comparator.comparingInt(Employee::getSalary));
        if (filteredEmployee.isEmpty()) {
            throw new NoDataFoundException();
        }
        return filteredEmployee.get().getSalary();
    }

    @Override
    public List<String> getTopTenHighestEarningEmployeeNames() {
        List<Employee> employeeList = getAllEmployees();
        Collections.sort(employeeList, Comparator.comparingInt(Employee::getSalary).reversed());
        List<String> filteredEmployeeNames = employeeList.stream().limit(10).map(Employee::getName).collect(Collectors.toList());
        return filteredEmployeeNames;
    }
    @CacheEvict(value="employees", allEntries=true)
    @Override
    public Employee createEmployee(CreateEmployee createEmployee) {
        Optional<String> response = restTemplateExecutor.execute("create", HttpMethod.POST, createEmployee, String.class);
        if (response.isEmpty()) {
            throw new EmployeeCreationException("Error in creating employee");
        }
        return getCreatedEmployee(response.get());
    }

    private Employee getCreatedEmployee(String responseData) {
        CreateEmployeesResponse response = null;
        try {
            response = Utils.getObjectMapper().readValue(responseData, CreateEmployeesResponse.class);
        } catch (JsonProcessingException e) {
            logger.error("Error while parsing Json: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return response.getData();
    }

    @CacheEvict(value="employees", allEntries=true)
    @Override
    public String deleteEmployeeById(String id) {
        Employee employee = getEmployeeById(id);
        restTemplateExecutor.execute("delete/" + id, HttpMethod.DELETE, null, null);
        return employee.getName();
    }
}
