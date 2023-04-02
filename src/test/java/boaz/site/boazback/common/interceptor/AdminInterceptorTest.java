package boaz.site.boazback.common.interceptor;

import boaz.site.boazback.BaseControllerTest;
import boaz.site.boazback.admin.user.controller.AdminUserController;
import boaz.site.boazback.common.domain.TokenType;
import boaz.site.boazback.common.exception.ControllerExceptionHandler;
import boaz.site.boazback.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.http.Cookie;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminInterceptorTest  extends BaseControllerTest {

    private Cookie memberAccessToken;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new AdminUserController(adminUserService),new ControllerExceptionHandler())
                .addInterceptors(new AdminInterceptor(objectMapper,jwtUtil,cookieUtil))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .alwaysDo(print())
                .build();
        setUpUser();
        setUpToken();
        User build = User.builder()
                .id(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"))
                .email("example@naver.com")
                .password("")
                .name("bob")
                .section(3)
                .year("16")
                .birthDate("0000-00-00")
                .build();
        String memberAccessTokenStr = jwtService.getToken(TokenType.ACCESS, build);
        memberAccessToken = new Cookie("accessToken", memberAccessTokenStr);
    }


    @Test
    void admin_권한_있을_경우() throws Exception {
        mockMvc.perform(get("/admin/users/roles")
                        .cookie(accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("MEMBER,ADMIN"))
        ;
    }

    @Test
    void admin_권한_없을_경우() throws Exception {
        mockMvc.perform(get("/admin/users/roles")
                        .cookie(memberAccessToken))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

}
