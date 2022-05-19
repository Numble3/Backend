package com.numble.team3.common.aop.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

  @Pointcut("execution(* com.numble.team3.common.advice.CommonRestControllerAdvice.*(..))")
  private void commonAdvice(){};

  @Pointcut("execution(* com.numble.team3.account..*(..))")
  private void account(){};

  @Pointcut("execution(* com.numble.team3.admin..*(..))")
  private void admin(){};

  @Pointcut("execution(* com.numble.team3.comment..*(..))")
  private void comment(){};

  @Pointcut("execution(* com.numble.team3.converter..*(..))")
  private void converter(){};

  @Pointcut("execution(* com.numble.team3.like..*(..))")
  private void like(){};

  @Pointcut("execution(* com.numble.team3.sign..*(..))")
  private void sign(){};

  @Pointcut("execution(* com.numble.team3.video..*(..))")
  private void video(){};
  
  @Pointcut(
    "commonAdvice() || account() || admin() || comment() || converter() || like() || sign() || video()"
  )
  public void loggingAopTarget(){};
}
