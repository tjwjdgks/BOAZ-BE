package boaz.site.boazback.common.resolver;

import boaz.site.boazback.common.domain.JwtInfo;
import boaz.site.boazback.common.domain.JwtLogin;
import boaz.site.boazback.common.domain.ResendEmailJwt;
import boaz.site.boazback.common.domain.ResendEmailToken;
import boaz.site.boazback.common.exception.JwtException;
import boaz.site.boazback.common.util.CookieUtil;
import boaz.site.boazback.common.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmailCheckArgumentResolver implements HandlerMethodArgumentResolver {

    private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isResendEmailJwtAnnotation = parameter.getParameterAnnotation(ResendEmailJwt.class)!=null;
        boolean isResendEmailTokenClass = ResendEmailToken.class.equals(parameter.getParameterType());
        return isResendEmailJwtAnnotation && isResendEmailTokenClass;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String certificateToken = cookieUtil.extractTokenFromRequest(request, "certificateToken");
        if(certificateToken == null){
            throw JwtException.TOKEN_NOT_FOUND;
        }
        Map<String,Object> data = jwtUtil.getClaimFromToken(certificateToken,"certificate");
        ResendEmailToken resendEmailToken = objectMapper.convertValue(data, ResendEmailToken.class);
        return resendEmailToken;
    }
}
