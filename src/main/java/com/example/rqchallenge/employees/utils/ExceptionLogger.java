package com.example.rqchallenge.employees.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionLogger {
  private final Logger logger = LoggerFactory.getLogger(ExceptionLogger.class);

  @AfterThrowing(
      pointcut =
          "execution(* com.example.rqchallenge.employees.service.EmployeeService.*(..)))||execution(* com.example.rqchallenge.employees.service.EmployeeService.*(..)))",
      throwing = "ex")
  public void logMethodException(JoinPoint joinPoint, Exception ex) {
    String methodName = joinPoint.getSignature().getName();
    logger.error("Exception in method: {} with message: {}", methodName, ex.getMessage());
  }
}
