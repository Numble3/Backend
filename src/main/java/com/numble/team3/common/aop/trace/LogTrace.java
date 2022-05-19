package com.numble.team3.common.aop.trace;

public interface LogTrace {
  TraceStatus begin(String message);

  void end(TraceStatus status);

  void exception(TraceStatus status, Exception e);
}