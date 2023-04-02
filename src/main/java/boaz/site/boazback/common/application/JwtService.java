package boaz.site.boazback.common.application;

import boaz.site.boazback.common.domain.ResendEmailToken;
import boaz.site.boazback.common.domain.TokenType;
import boaz.site.boazback.common.util.CookieUtil;
import boaz.site.boazback.common.util.JwtUtil;
import boaz.site.boazback.user.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    public static final String REFRESH_TOKEN = "refreshToken";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String CERTIFICATE_TOKEN = "certificateToken";

    private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;



    public void getTokens(User user, HttpServletResponse response) {
        String refreshToken = getToken(TokenType.REFRESH,user);
        String accessToken = getToken(TokenType.REFRESH,user);
        cookieUtil.setHttpCookie(response, REFRESH_TOKEN,refreshToken);
        cookieUtil.setHttpCookie(response, ACCESS_TOKEN,accessToken);
    }

    public String getToken(TokenType tokenType, User user) {
        return jwtUtil.generateToken(user,tokenType);
    }

    public String getToken(String email){
        ResendEmailToken resendEmailToken = new ResendEmailToken();
        resendEmailToken.setEmail(email);
        String certificateToken = jwtUtil.generateResendEmailToken(resendEmailToken);
        return certificateToken;
    }


    public void refreshUserToken(HttpServletRequest request, HttpServletResponse response) {
        String token = cookieUtil.extractTokenFromRequest(request, REFRESH_TOKEN);
        Map<String,Object> user = jwtUtil.getClaimFromToken(token,"user");
        User userInfo = objectMapper.convertValue(user, User.class);
        String accessToken = getToken(TokenType.ACCESS, userInfo);
        cookieUtil.setHttpCookie(response, ACCESS_TOKEN,accessToken);
        cookieUtil.setHttpCookie(response, REFRESH_TOKEN,token);
    }

    public void setCertificateToken(@Email @NotEmpty String email, HttpServletResponse response) {
        String certificateToken = getToken(email);
        cookieUtil.setHttpCookie(response, CERTIFICATE_TOKEN, certificateToken);
    }

    public void removeTokens(HttpServletRequest request,HttpServletResponse response) {
        List<String> cookieNames = new ArrayList<>();
        cookieNames.add(ACCESS_TOKEN);
        cookieNames.add(REFRESH_TOKEN);
        cookieNames.add(CERTIFICATE_TOKEN);
        cookieUtil.deleteCookies(request,response, cookieNames);
    }

    public void removeToken(String tokenName,HttpServletRequest request,HttpServletResponse response) {
        List<String> cookieNames = new ArrayList<>();
        cookieNames.add(tokenName);
        cookieUtil.deleteCookies(request,response, cookieNames);
    }

    public Date getAccessTokenExpiredTime(HttpServletRequest request, String tokenName){
        String token = cookieUtil.extractTokenFromRequest(request, tokenName);
        return jwtUtil.getExpirationDateFromToken(token);
    }

}
