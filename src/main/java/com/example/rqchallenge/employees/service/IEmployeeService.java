package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.models.CreateEmployee;
import com.example.rqchallenge.employees.models.Employee;
import java.util.List;

public interface IEmployeeService {
    
    public List<Employee> getAllEmployees();

    public List<Employee> getEmployeesByNameSearch(String searchString);

    public Employee getEmployeeById(String id);

    public Integer getHighestSalaryOfEmployees();

    public List<String> getTopTenHighestEarningEmployeeNames();

    public Employee createEmployee(CreateEmployee createEmployee);
    
    public String deleteEmployeeById(String id);
}
