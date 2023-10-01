package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.models.CreateEmployee;
import com.example.rqchallenge.employees.models.Employee;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import java.util.List;

public interface IEmployeeService {

  @Retryable(
      value = {Exception.class},
      maxAttemptsExpression = "${retry.maxAttempts:2}",
      backoff = @Backoff(delayExpression = "${retry.delay:2000}"))
  public List<Employee> getAllEmployees();

  @Retryable(
      value = {Exception.class},
      maxAttemptsExpression = "${retry.maxAttempts:2}",
      backoff = @Backoff(delayExpression = "${retry.delay:2000}"))
  public List<Employee> getEmployeesByNameSearch(String searchString);

  public Employee getEmployeeById(String id);

  public Integer getHighestSalaryOfEmployees();

  public List<String> getTopTenHighestEarningEmployeeNames();

  public Employee createEmployee(CreateEmployee createEmployee);

  public String deleteEmployeeById(String id);
}
