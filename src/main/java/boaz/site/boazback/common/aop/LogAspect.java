package boaz.site.boazback.common.aop;

import com.google.common.base.Joiner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Aspect
public class LogAspect {
    private final Logger log = LogManager.getLogger();

    @Around("@annotation(boaz.site.boazback.common.domain.Logging)")
    public Object requestLogging(ProceedingJoinPoint pjp) throws Throwable {
        String params = getRequestParams(); // request 값 가져오기

        long startAt = System.currentTimeMillis();

        log.info("-----------> REQUEST : {}({}) = {}", pjp.getSignature().getDeclaringTypeName(),
                pjp.getSignature().getName(), params);

        Object result = pjp.proceed(); // 4

        long endAt = System.currentTimeMillis();

        log.info("-----------> RESPONSE : {}({}) = {} ({}ms)", pjp.getSignature().getDeclaringTypeName(),
                pjp.getSignature().getName(), result, endAt - startAt);
        return result;
    }

    private String paramMapToString(Map<String, String[]> paramMap) {
        return paramMap.entrySet().stream()
                .map(entry -> String.format("%s -> (%s)",
                        entry.getKey(), Joiner.on(",").join(entry.getValue())))
                .collect(Collectors.joining(", "));
    }

    // Get request values
    private String getRequestParams() {

        String params = "없음";
        RequestAttributes requestAttributes = RequestContextHolder
                .getRequestAttributes(); // 3
        if (requestAttributes == null){
            throw new IllegalStateException("null point ex");
        }
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(servletRequestAttributes == null) {
            throw new IllegalStateException("servletRequestAttributes null error");
        }
        HttpServletRequest request = servletRequestAttributes.getRequest();
        Map<String, String[]> paramMap = request.getParameterMap();
        if (!paramMap.isEmpty()) {
            params = " [" + paramMapToString(paramMap) + "]";
        }
        return params;
    }
}


