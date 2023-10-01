package com.example.rqchallenge.employees.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @JsonProperty("id")
    String id;
    @JsonProperty("employee_salary")
    int salary;
    @JsonProperty("employee_age")
    int age;
    @JsonProperty("employee_name")
    String name;
    @JsonProperty("profile_image")
    String profileImage;
}
