package com.example.rqchallenge.employees.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
  @Schema(name = "Employee Id", example = "1")
  @JsonProperty("id")
  String id;

  @Schema(name = "Employee Salary", example = "100")
  @JsonProperty("employee_salary")
  int salary;

  @Schema(name = "Employee Age", example = "30")
  @JsonProperty("employee_age")
  int age;

  @Schema(name = "Employee Name", example = "sample")
  @JsonProperty("employee_name")
  String name;

  @Schema(name = "Profile Image", example = "sample_image_link")
  @JsonProperty("profile_image")
  String profileImage;
}
