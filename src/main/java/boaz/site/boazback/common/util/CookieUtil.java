package boaz.site.boazback.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
@Slf4j
public class CookieUtil {

    private static final Long DELETE_COOKIE_AGE=0L;
    public HttpServletResponse setHttpCookie(HttpServletResponse response,String name,String value) {
        log.info("setHttpCookie start - {}",name);
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("None")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
        log.info("setHttpCookie end - {}",name);
        return response;
     }

    public HttpServletResponse deleteHttpCookie(HttpServletResponse response,String name) {
        log.info("deleteHttpCookie start - {}",name);
        ResponseCookie cookie = ResponseCookie.from(name, null)
                .sameSite("None")
                .maxAge(DELETE_COOKIE_AGE)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
        log.info("deleteHttpCookie end - {}",name);
        return response;
    }
    // token 추출하기
    public String extractTokenFromRequest(HttpServletRequest request,String name){
        log.info("extractTokenFromRequest start");
        final Cookie[] cookies = request.getCookies();
        if(cookies == null) return null;
        for(int i=0; i <cookies.length;i++){
            if(cookies[i].getName().equals(name)){
                log.info("extractTokenFromRequest end - find cookie");
                return cookies[i].getValue();
            }
        }
        log.info("extractTokenFromRequest end");
        return null;
    }

    public HttpServletResponse deleteCookies(HttpServletRequest request, HttpServletResponse response){
        log.info("deleteCookies1 start");
        Cookie[] cookies = request.getCookies();            // 요청정보로부터 쿠키를 가져온다.
        if(cookies!=null){
            for(int i = 0 ; i<cookies.length; i++){
                // 쿠키 배열을 반복문으로 돌린다.
                response = deleteHttpCookie(response,cookies[i].getName());
            }
        }
        log.info("deleteCookies1 end");
        return response;
    }

    public HttpServletResponse deleteCookies(HttpServletRequest request, HttpServletResponse response, List<String> cookieNames){
        log.info("deleteCookies2 start");
        Cookie[] cookies = request.getCookies();            // 요청정보로부터 쿠키를 가져온다.
        if(cookies!=null){
            for(int i = 0 ; i<cookies.length; i++){
                // 쿠키 배열을 반복문으로 돌린다.
                if(cookieNames.contains(cookies[i].getName())){
                    response = deleteHttpCookie(response,cookies[i].getName());
                }
            }
        }
        log.info("deleteCookies2 end");
        return response;
    }
}
