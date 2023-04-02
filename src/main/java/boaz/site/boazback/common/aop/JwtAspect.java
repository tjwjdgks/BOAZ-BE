package boaz.site.boazback.common.aop;


import boaz.site.boazback.common.domain.TokenType;
import boaz.site.boazback.common.exception.JwtException;
import boaz.site.boazback.common.util.CookieUtil;
import boaz.site.boazback.common.util.JwtUtil;
import boaz.site.boazback.user.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Aspect
@Component
@RequiredArgsConstructor
public class JwtAspect {

    private final ObjectMapper objectMapper;
    private final JwtUtil jwtService;
    private final CookieUtil cookieUtil;
    private final Logger logger = LogManager.getLogger();

    @Around("@annotation(boaz.site.boazback.common.domain.CheckJwt)")
    public Object requestLogging(ProceedingJoinPoint pjp) throws Throwable {
        try {
            logger.info("start check accessToken");
            String accessToken = getToken("accessToken");
            logger.info("end check accessToken");
            return checkToken(accessToken,pjp);
        }catch(Exception e) {
            throw e;
        }
    }

    @Around("@annotation(boaz.site.boazback.common.domain.CheckJwt2)")
    public Object callRefreshToken(ProceedingJoinPoint pjp) throws Throwable {

        try{
            logger.info("start check refreshToken");
            String token = getToken("refreshToken"); // request 값 가져오기
            logger.info("end check refreshToken");
            return checkToken(token,pjp);
        }catch(Exception e){
            //권한이 없다고 보내준다.
            throw e;
        }
    }

    @Around("@annotation(boaz.site.boazback.common.domain.NoRefreshtJwt)")
    public Object callNoRefreshToken(ProceedingJoinPoint pjp) throws Throwable {
        try {
            logger.info("start check accessToken");
            String accessToken = getToken("accessToken");
            logger.info("end check accessToken");
            return noCheckToken(accessToken,pjp);
        }catch(Exception e) {
            throw e;
        }
    }

    @Around("@annotation(boaz.site.boazback.common.domain.CheckResendJwt)")
    public Object checkResendEmailToken(ProceedingJoinPoint pjp) throws Throwable {
        try {
            logger.info("start check certificateToken");
            String certificateToken = getToken("certificateToken");
            logger.info("end check certificateToken");
            jwtService.isTokenExpired(certificateToken);
            return pjp.proceed();
        }catch(Exception e) {
            throw e;
        }
    }


private Object checkToken(String token,ProceedingJoinPoint pjp) throws Throwable {
    // request 값 가져오기
    logger.info("start code");
    boolean isAccessTokenExpired = jwtService.isTokenExpired(token);
    if(isAccessTokenExpired){
        logger.info("start check refreshToken");
        token = getToken("refreshToken");
        jwtService.isRefreshTokenExpired(token);
        logger.info("end check refreshToken");
    }
    Object result = pjp.proceed();
    if(isAccessTokenExpired){
        createToken(token);
    }
    logger.info("end code");
    return result;
}

    private Object noCheckToken(String token,ProceedingJoinPoint pjp) throws Throwable {
        // request 값 가져오기
        logger.info("start code");
        jwtService.isTokenExpired(token);
        logger.info("end code");
        return pjp.proceed();
    }

    // Get request values
    private String getToken(String name) {
        String token = "";
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes(); // 3
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            token = cookieUtil.extractTokenFromRequest(request, name);
            if(token == null){
                logger.error("token not found error issued");
                throw JwtException.TOKEN_NOT_FOUND;
            }
            return token;
        }
        return token;
    }

    private void createToken(String token){
        logger.info("create token start");
        Map<String, Object> data = jwtService.getClaimFromToken(token,"user");
        User userInfo = objectMapper.convertValue(data, User.class);
        String newToken = jwtService.generateToken(userInfo, TokenType.ACCESS);
        HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getResponse();
        cookieUtil.setHttpCookie(response,"accessToken",newToken);
        logger.info("create token end");
    }

}
