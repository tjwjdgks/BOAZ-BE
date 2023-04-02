package boaz.site.boazback.intro.controller;

import boaz.site.boazback.BaseControllerTest;
import boaz.site.boazback.intro.dto.IntroContentRegister;
import boaz.site.boazback.intro.dto.IntroInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class IntroRestControllerTest extends BaseControllerTest {

    @BeforeEach
    void setUp() {
        baseSetUp();
    }

    @Test
    void getMemberIntro() throws Exception {
        List<IntroInfo> result = new ArrayList<IntroInfo>();
        result.add(new IntroInfo());
        given(introService.getIntroList(any())).willReturn(result);
        mockMvc.perform(get("/intro")
                        .param("page", "0")
                        .param("size", "5")
                        .cookie(accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload").isArray());
    }

    @Test
    void getIntroTotalPages() throws Exception {
        given(introService.getTotalPages(anyLong())).willReturn(1L);
        mockMvc.perform(get("/intro/page")
                        .param("size", "5")
                        .cookie(accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload").value(1L));
    }

    @Test
    void postMemberIntro() throws Exception {
        IntroInfo result = new IntroInfo();
        IntroContentRegister introContentRegister = new IntroContentRegister("123123");
        result.setId(1L);
        given(introService.saveIntro(any(), anyString())).willReturn(result);
        mockMvc.perform(post("/intro")
                        .content(objectMapper.writeValueAsString(introContentRegister))
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessToken))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.payload.id").value(1));
    }


    @Test
    void postMemberIntro_valid_error() throws Exception {
        IntroInfo result = new IntroInfo();
        IntroContentRegister introContentRegister = new IntroContentRegister("");
        result.setId(1L);
        given(introService.saveIntro(any(), anyString())).willReturn(result);
        mockMvc.perform(post("/intro")
                        .content(objectMapper.writeValueAsString(introContentRegister))
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessToken))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void putMemberIntro() throws Exception {
        IntroInfo result = new IntroInfo();
        result.setContent("ffff");
        IntroContentRegister introContentRegister = new IntroContentRegister("hello");
        given(introService.updateComment(any(), anyLong(), anyString())).willReturn(result);
        mockMvc.perform(put("/intro/{id}", 1)
                        .content(objectMapper.writeValueAsString(introContentRegister))
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.content").value("ffff"));
    }

    @Test
    void putMemberIntro_valid_error() throws Exception {
        IntroInfo result = new IntroInfo();
        IntroContentRegister introContentRegister = new IntroContentRegister();
        given(introService.updateComment(any(), anyLong(), anyString())).willReturn(result);
        mockMvc.perform(put("/intro/{id}", 1)
                        .content(objectMapper.writeValueAsString(introContentRegister))
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessToken)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteMemberIntro() throws Exception {
        mockMvc.perform(delete("/intro/{id}", 1)
                        .cookie(accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.formEnum").value("PASS"));
        verify(introService).deleteComment(any(), anyLong());
    }


}
