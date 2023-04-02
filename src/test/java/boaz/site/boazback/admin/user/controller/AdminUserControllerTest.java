package boaz.site.boazback.admin.user.controller;

import boaz.site.boazback.BaseControllerTest;
import boaz.site.boazback.user.domain.Role;
import boaz.site.boazback.user.dto.UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminUserControllerTest  extends BaseControllerTest {

    @BeforeEach
    void setUp() {
        baseSetUp();
    }

    @Test
    void getUserList() throws Exception {
        List<UserInfo.SimpleLevel> userSimpleLevelList = new ArrayList<>();
        UserInfo.SimpleLevel fakedata = UserInfo.SimpleLevel.builder()
                .id(UUID.randomUUID())
                .year("16기")
                .role(Role.ADMIN)
                .track(3)
                .name("test")
                .build();
        userSimpleLevelList.add(fakedata);
        given(adminUserService.getUserList((Pageable) any())).willReturn(userSimpleLevelList);
        mockMvc.perform(get("/admin/users")
                        .param("page","0")
                        .param("size","5")
                        .param("sort","name")
                        .param("direction","ASC")
                        .cookie(accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("test"));
    }

}
