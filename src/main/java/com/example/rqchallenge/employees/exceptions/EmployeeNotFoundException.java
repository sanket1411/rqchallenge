package com.example.rqchallenge.employees.exceptions;

public class EmployeeNotFoundException extends RuntimeException {
  public EmployeeNotFoundException(String id) {
    super(String.format("Employee with Id %s not found", id));
  }
}
