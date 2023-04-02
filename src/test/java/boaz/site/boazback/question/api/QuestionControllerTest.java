package boaz.site.boazback.question.api;


import boaz.site.boazback.BaseControllerTest;
import boaz.site.boazback.question.dto.QuestionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


 class QuestionControllerTest extends BaseControllerTest {


    private QuestionDto questionDto;

    @BeforeEach
    void setUp() {
        questionDto  = new QuestionDto("bob","example@naver.com","test","test");
    }

    @Test
    void 문의하기() throws Exception {
        given(questionService.sendInfo(any())).willReturn(questionDto.transForm());
        mockMvc.perform(post("/question")
                        .content(objectMapper.writeValueAsString(questionDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.formEnum").value("PASS"))
                .andExpect(jsonPath("$.payload.email").value("example@naver.com"));
    }

    @Test
    void 특정문의조회하기() throws Exception{
        given(questionService.findQuestion(anyLong())).willReturn(questionDto.transForm());
        mockMvc.perform(get("/question/{uid}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.formEnum").value("PASS"))
                .andExpect(jsonPath("$.payload.email").value("example@naver.com"));
    }

    @Test
    void 문의서식검증실패() throws Exception{
        questionDto  = new QuestionDto("","","","");
        mockMvc.perform(post("/question")
                        .content(objectMapper.writeValueAsString(questionDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.errors",hasSize(4)));
    }

}
