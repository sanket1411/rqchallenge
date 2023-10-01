package com.example.rqchallenge.employees.exceptions;

public class NoDataFoundException extends RuntimeException {
  public NoDataFoundException() {
    super(String.format("No Data found."));
  }
}
