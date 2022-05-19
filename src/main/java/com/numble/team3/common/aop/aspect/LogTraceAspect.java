package com.numble.team3.common.aop.aspect;

import com.numble.team3.common.aop.trace.LogTrace;
import com.numble.team3.common.aop.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Aspect
@Configuration
public class LogTraceAspect {
  private final LogTrace logTrace;

  public LogTraceAspect(LogTrace logTrace) {
    this.logTrace = logTrace;
  }

  @Around("com.numble.team3.common.aop.aspect.Pointcuts.loggingAopTarget()")
  public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
    TraceStatus status = null;

    try {
      String message = joinPoint.getSignature().toShortString();
      status = logTrace.begin(message);

      Object result = joinPoint.proceed();

      logTrace.end(status);
      return result;
    } catch (Exception e) {
      logTrace.exception(status, e);
      throw e;
    }
  }
}