package boaz.site.boazback.common.api;

import boaz.site.boazback.BaseControllerTest;
import boaz.site.boazback.user.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends BaseControllerTest {


    @BeforeEach
    void setUp() {
        baseSetUp();
    }

    @Test
    void 토큰체크()throws Exception {
        mockMvc.perform(get("/auth/check")
                        .cookie(accessToken)
                        .cookie(refreshToken)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200));
    }

    @Test
    void 토큰체크_accesstoken_없음()throws Exception {
        mockMvc.perform(get("/auth/check")
                        .cookie(refreshToken)
                ).andDo(print())
                .andExpect(status().is4xxClientError());
    }

}
