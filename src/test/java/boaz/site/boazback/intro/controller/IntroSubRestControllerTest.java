package boaz.site.boazback.intro.controller;

import boaz.site.boazback.BaseControllerTest;
import boaz.site.boazback.intro.dto.IntroSubCommentInfo;
import boaz.site.boazback.intro.dto.IntroSubContentRegister;
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


class IntroSubRestControllerTest extends BaseControllerTest {

    @BeforeEach
    void setUp() {
        baseSetUp();
    }

    @Test
    void getIntroSubComments() throws Exception {
        List<IntroSubCommentInfo> result = new ArrayList<>();
        result.add(new IntroSubCommentInfo());
        given(introSubCommentService.getSubCommentsByIntro(anyLong())).willReturn(result);
        mockMvc.perform(get("/introsub/{id}", 1)
                        .cookie(accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200));
    }

    @Test
    void postIntroSubComment() throws Exception {
        IntroSubCommentInfo result = new IntroSubCommentInfo();
        IntroSubContentRegister introSubContentRegister = new IntroSubContentRegister("aaa",1L);
        result.setContents("ddd");
        given(introSubCommentService.saveSubComment(any(), anyString(), anyLong())).willReturn(result);
        mockMvc.perform(post("/introsub")
                        .content(objectMapper.writeValueAsString(introSubContentRegister))
                        .contentType(MediaType.APPLICATION_JSON)
                .cookie(accessToken))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.payload.contents").value("ddd"));
    }

    @Test
    void putIntroSubComment() throws Exception {
        IntroSubCommentInfo result = new IntroSubCommentInfo();
        result.setContents("123");
        IntroSubContentRegister introSubContentRegister = new IntroSubContentRegister("aaa",1L);
        given(introSubCommentService.updateSubComment(any(), anyLong(), anyString())).willReturn(result);
        mockMvc.perform(put("/introsub/{id}", 1)
                        .content(objectMapper.writeValueAsString(introSubContentRegister))
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.contents").value("123"))
        ;
    }

    @Test
    void deleteIntroSubComment() throws Exception {
        mockMvc.perform(delete("/introsub/{id}",1)
                        .cookie(accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200));
        verify(introSubCommentService).deleteSubComment(any(), anyLong());
    }
}
