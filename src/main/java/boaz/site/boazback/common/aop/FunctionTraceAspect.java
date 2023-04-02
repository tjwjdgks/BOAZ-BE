package boaz.site.boazback.common.aop;

import io.opentracing.Span;
import io.opentracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class FunctionTraceAspect {

    private final Tracer tracer;

    @Around("@annotation(boaz.site.boazback.common.domain.TracingFunction)")
    public Object requestLogging(ProceedingJoinPoint pjp) throws Exception {
        String targetMethod = pjp.getSignature().getName();
        Span span = tracer.buildSpan(targetMethod).start();
        Object result = null;
        try {
            span.setTag("function", targetMethod);
            result = pjp.proceed();
            return result;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            span.finish();
        }
    }
}
