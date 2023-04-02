package boaz.site.boazback.common.interceptor;

import boaz.site.boazback.common.exception.AuthorityException;
import boaz.site.boazback.common.exception.JwtException;
import boaz.site.boazback.common.util.CookieUtil;
import boaz.site.boazback.common.util.JwtUtil;
import boaz.site.boazback.user.domain.Role;
import boaz.site.boazback.user.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("AdminInterceptor start");

        String token = cookieUtil.extractTokenFromRequest(request, "accessToken");
        if(token == null ){
            throw JwtException.TOKEN_NOT_FOUND;
        }
        Map<String,Object> data = jwtUtil.getClaimFromToken(token,"user");
        User user = objectMapper.convertValue(data, User.class);

        if(user.getRole() == Role.ADMIN){
            return true;
        }else{
            throw AuthorityException.ADMIN_ERROR;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("AdminInterceptor end");
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

}
