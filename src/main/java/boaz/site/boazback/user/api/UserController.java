package boaz.site.boazback.user.api;

import boaz.site.boazback.common.application.JwtService;
import boaz.site.boazback.common.domain.*;
import boaz.site.boazback.user.application.UserService;
import boaz.site.boazback.user.dto.*;
import boaz.site.boazback.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Profile("!prd")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final RedirectionConfig redirectionConfig;

    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
    @TracingFunction
    public Result registerUser(@RequestBody @Valid UserDto userDto, HttpServletResponse response){
        log.info("registerUser start");
        Result result = new Result().resultSuccess();
        userService.registerUser(userDto);
        jwtService.setCertificateToken(userDto.getEmail(),response);
        result.setData(true);
        log.info("registerUser end");
        return result;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public Result loginUser(@RequestBody @Valid UserLogin userLogin, HttpServletResponse response) {
        Result result = new Result().resultSuccess();
        log.info("loginUser start");
        User user = userService.loginUser(userLogin);
        jwtService.getTokens(user, response);
        if(!user.isEmailCheck()){
            jwtService.setCertificateToken(user.getEmail(),response);
        }
        result.setData(user);
        log.info("loginUser end");
        return result;
    }

    @GetMapping("/refresh")
    @CheckJwt2
    public Result refreshUserToken(HttpServletRequest request,HttpServletResponse response){
        log.info("refreshUserToken start");
        jwtService.refreshUserToken(request, response);
        log.info("refreshUserToken end");
        return new Result().resultSuccess();
    }

    @GetMapping("/logout")
    @NoRefreshtJwt
    public Result logoutUser(HttpServletRequest request,HttpServletResponse response) {
        log.info("logoutUser start");
        jwtService.removeTokens(request,response);
        log.info("logoutUser end");
        return new Result().resultSuccess();
    }


    @GetMapping("/certifications/signup-confirm")
    public void certificationSignupConfirm(@RequestParam("c") String certification, @RequestParam ("m") String encodedEmail,HttpServletRequest request,HttpServletResponse response) throws IOException {
        log.info("certificationSignupConfirm start");
        userService.certificationSignupConfirm(certification,encodedEmail);
        jwtService.removeToken("certificateToken",request,response);
        log.info("certificationSignupConfirm end");
        response.sendRedirect(redirectionConfig.getRedirectionUrl());
    }

    @PostMapping("/resend-join-email")
    @CheckResendJwt
    public Result resendEmailForJoin(@ResendEmailJwt ResendEmailToken resendEmailToken) {
        log.info("certificationSignupConfirm start");
        userService.resendJoinConfirmMail(resendEmailToken.getEmail());
        log.info("certificationSignupConfirm end");
        return new Result().resultSuccess();
    }


    @PatchMapping("/certifications/reissue-password-confirm")
    public Result certificationReissuePasswordConfirm(@RequestParam("certification") String certification, @RequestBody @Valid PasswordDto passwordDto) {
        log.info("certificationReissuePasswordConfirm start");
        userService.certificationReissuePasswordConfirm(certification, passwordDto.getNewPassword());
        log.info("certificationReissuePasswordConfirm end");
        return new Result().resultSuccess();
    }

    @PostMapping("/passwords/resend-reissued-password-email")
    @ResponseStatus(HttpStatus.CREATED)
    public Result resendReissuedPasswordEmail(@RequestBody @Valid FindEmailDto findEmailDto, Errors errors) {
        if(errors.hasErrors()){
            return new Result().resultBadRequest(errors);
        }
        log.info("reissuePassword start");
        userService.reissueUserPasswordByEmail(findEmailDto);
        log.info("reissuePassword end");
        return new Result().resultSuccess();
    }

    @PostMapping("/passwords/reissue")
    @ResponseStatus(HttpStatus.CREATED)
    public Result reissuePassword(@RequestBody @Valid FindEmailDto findEmailDto, Errors errors){
        if(errors.hasErrors()){
            return new Result().resultBadRequest(errors);
        }
        log.info("reissuePassword start");
        userService.reissueUserPasswordByEmail(findEmailDto);
        log.info("reissuePassword end");
        return new Result().resultSuccess();
    }

}
