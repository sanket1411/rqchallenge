package com.example.rqchallenge.employees.models;

import lombok.Data;

import java.util.List;

@Data
public class CreateEmployeesResponse {
  String status;
  Employee data;
  String message;
}
