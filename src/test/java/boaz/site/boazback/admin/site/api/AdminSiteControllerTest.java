package boaz.site.boazback.admin.site.api;

import boaz.site.boazback.BaseControllerTest;
import boaz.site.boazback.admin.site.dto.SiteDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class AdminSiteControllerTest extends BaseControllerTest {

    @BeforeEach
    void setUp() {
        baseSetUp();
    }

    @Test
    void updateSiteInformation() throws Exception {
        SiteDto.Request siteRequest = new SiteDto.Request("", "");
        given(adminSiteService.updateSiteInformation(any())).willReturn(true);
        mockMvc.perform(post("/admin/site")
                        .content(objectMapper.writeValueAsBytes(siteRequest))
                        .cookie(accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("true"));
    }
}
