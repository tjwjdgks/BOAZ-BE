package boaz.site.boazback.common.api;

import boaz.site.boazback.common.application.JwtService;
import boaz.site.boazback.common.domain.NoRefreshtJwt;
import boaz.site.boazback.common.domain.Result;
import boaz.site.boazback.user.dto.TokenCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Profile("!prd")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JwtService jwtService;

    @GetMapping("/auth/check")
    @NoRefreshtJwt
    public Result authCheck(HttpServletRequest request) {
        log.info("start authCheck");
        Date expiredTime = jwtService.getAccessTokenExpiredTime(request, "accessToken");
        Result result = new Result().resultSuccess();
        TokenCheck tokenCheck = new TokenCheck(expiredTime, false);
        result.setData(tokenCheck);
        log.info("end authCheck");
        return result;
    }
}
