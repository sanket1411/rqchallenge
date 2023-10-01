package com.example.rqchallenge.employees.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExecutionTimeLogger {
  private final Logger logger = LoggerFactory.getLogger(ExecutionTimeLogger.class);

  @Around("execution(* com.example.rqchallenge.employees.restexecutor.RestTemplateExecutor.*(..))")
  public Object logRestTemplateExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long startTime = System.currentTimeMillis();

    // Execute the method
    Object result = joinPoint.proceed();

    long endTime = System.currentTimeMillis();
    long timeTaken = endTime - startTime;

    String methodName = joinPoint.getSignature().getName();
    logger.info("Method {} took {} ms to execute.", methodName, timeTaken);
    return result;
  }

  @Around("execution(* com.example.rqchallenge.employees.service.EmployeeService.*(..))")
  public Object logServiceArgumentsAndExecutionTime(ProceedingJoinPoint joinPoint)
      throws Throwable {
    long startTime = System.currentTimeMillis();

    // Execute the method
    Object result = joinPoint.proceed();

    long endTime = System.currentTimeMillis();
    long timeTaken = endTime - startTime;

    String methodName = joinPoint.getSignature().getName();
    Object[] args = joinPoint.getArgs();
    logger.info("Method {} with arguments {} took {} ms to execute.", methodName, args, timeTaken);
    return result;
  }
}
