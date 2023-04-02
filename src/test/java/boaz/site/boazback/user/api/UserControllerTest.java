package boaz.site.boazback.user.api;

import boaz.site.boazback.BaseControllerTest;
import boaz.site.boazback.common.exception.ErrorCode;
import boaz.site.boazback.common.exception.UserException;
import boaz.site.boazback.user.dto.UserDto;
import boaz.site.boazback.user.dto.UserLogin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class UserControllerTest extends BaseControllerTest {


    UserDto userDto;

    @BeforeEach
    void setUp() {
        baseSetUp();
        userDto = UserDto.builder()
                .email("example@naver.com")
                .password("hello01")
                .name("bob")
                .section(3)
                .year("16")
                .authenticationCode("")
                .birthDate("0000-00-00")
                .build();
    }

    @Test
    void 유저등록하기() throws Exception {
        given(userService.registerUser(any())).willReturn(userDto.transForm());
        mockMvc.perform(post("/user/join")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void 유저등록검증실패() throws Exception {
        userDto.setEmail("");
        given(userService.registerUser(any())).willReturn(userDto.transForm());

        mockMvc.perform(post("/user/join")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value("400"))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field", is(in(Arrays.asList("email")))));
    }

    @Test
    void 유저로그인() throws Exception {
        UserLogin userLogin = new UserLogin("example@gmail.com", "hello01");
        given(userService.loginUser(any())).willReturn(userDto.transForm());
        mockMvc.perform(post("/user/login")
                        .content(objectMapper.writeValueAsString(userLogin))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(header().exists("Set-Cookie"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(200));
    }

    @Test
    void 유저로그인실패_유저없음() throws Exception {
        UserLogin userLogin = new UserLogin("example@gmail.com", "hello01");
        when(userService.loginUser(any())).thenThrow(new UserException(ErrorCode.EMAIL_ERROR));
        mockMvc.perform(post("/user/login")
                        .content(objectMapper.writeValueAsString(userLogin))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.statusCode").value(401));
    }


    @Test
    void 토큰재발급받기() throws Exception {
        mockMvc.perform(get("/user/refresh")
                        .cookie(refreshToken))
                .andDo(print());
    }

    @Test
    void 로그아웃() throws Exception {
        mockMvc.perform(get("/user/logout")
                        .cookie(accessToken, refreshToken))
                .andDo(print())
                .andExpect(jsonPath("$.statusCode").value(200));
    }

    @Test
    void 회원가입이메일인증() throws Exception {
        given(redirectionConfig.getRedirectionUrl()).willReturn("");
        mockMvc.perform(get("/user/certifications/signup-confirm")
                        .param("c", "1234")
                        .param("m", "af")
                )
                .andDo(print())
                .andExpect(redirectedUrl(""));
    }


    @Test
    void 회원가입이메일재전송() throws Exception {
        mockMvc.perform(post("/user/resend-join-email")
                        .cookie(certificateToken)
                )
                .andDo(print())
                .andExpect(jsonPath("$.statusCode").value(200));
    }


    @Test
    void 회원가입이메일재전송_토큰이없는경우() throws Exception {
        mockMvc.perform(post("/user/resend-join-email"))
                .andDo(print())
                .andExpect(jsonPath("$.statusCode").value(403));
    }

}
