package com.example.rqchallenge.employees.models;

import lombok.Data;

import java.util.List;

@Data
public class GetEmployeesResponse {
  String status;
  List<Employee> data;
  String message;
}
