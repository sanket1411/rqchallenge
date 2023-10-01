package com.example.rqchallenge.employees.exceptions;

public class EmployeeCreationException extends RuntimeException{
    public EmployeeCreationException(String message) {
        super(String.format(message));
    }
}