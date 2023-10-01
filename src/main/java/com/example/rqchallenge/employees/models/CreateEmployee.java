package com.example.rqchallenge.employees.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CreateEmployee {

    @Schema(name = "Employee Salary", example = "100")
    @JsonProperty("employee_salary")
    int salary;

    @Schema(name = "Employee Age", example = "30")
    @JsonProperty("employee_age")
    @Max(value=150)
    int age;

    @Schema(name = "Employee Name", example = "sample")
    @JsonProperty("employee_name")
    @Size(max = 512, message = "Max name size can be 512")
    @NotBlank(message = "Name is mandatory")
    String name;

    @Schema(name = "Profile Image", example = "sample_image_link")
    @Nullable
    @JsonProperty("profile_image")
    String profileImage;
}
