package com.example.rqchallenge.employees.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CreateEmployee {
    @JsonProperty("employee_salary")
    int salary;

    @JsonProperty("employee_age")
    @Max(value=150)
    int age;

    @JsonProperty("employee_name")
    @Size(max = 512, message = "Max name size can be 512")
    @NotBlank(message = "Name is mandatory")
    String name;

    @Nullable
    @JsonProperty("profile_image")
    String profileImage;
}
