package org.example.procotol;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Aspect
@Component
@Order(1)
public class RequestAspect {
    private static final Logger log = LoggerFactory.getLogger(RequestAspect.class);

    /**
     * 定义切入点
     */
    @Pointcut("execution(* org.example.consumer.*.*(..))")
    public void cutMethod() {

    }

    /**
     * 环绕切面
     *
     * @param joinPoint 切入点
     * @return 处理结果
     * @throws Throwable 异常
     */
    @Around("cutMethod()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String uri = request.getRequestURI();

        StopWatch stopWatch=new StopWatch();
        stopWatch.start();
        Object responseResult = joinPoint.proceed();
        stopWatch.stop();
        log.info("**********请求：{}，请求耗时：{} 秒",uri,stopWatch.getTotalTimeSeconds());
        return responseResult;
    }
}
