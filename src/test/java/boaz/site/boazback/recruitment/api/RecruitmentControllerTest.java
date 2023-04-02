package boaz.site.boazback.recruitment.api;

import boaz.site.boazback.BaseControllerTest;
import boaz.site.boazback.recruitment.domain.Recruitment;
import boaz.site.boazback.recruitment.dto.RecruitmentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class RecruitmentControllerTest extends BaseControllerTest {

    RecruitmentDto.RecruitmentResponse recruitmentResponse;
    @BeforeEach
    void setUp() {
        Recruitment recruitment = Recruitment.builder()
                .id(1L)
                .recruitmentUrl("1")
                .isRecruitmentDuration(true)
                .notificationUrl("2")
                .faqUrl("3")
                .build();
        recruitmentResponse = RecruitmentDto.RecruitmentResponse.translateEntityBuilder()
                .recruitment(recruitment)
                .translate();
    }


    @Test
    void recuritment조회하기() throws Exception {
        given(recruitmentService.getRecruitment()).willReturn(recruitmentResponse);
        mockMvc.perform(get("/recruitment"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("payload.recruitmentUrl").value("1"))
                .andExpect(jsonPath("payload.isRecruitmentDuration").value(true));
    }
}
