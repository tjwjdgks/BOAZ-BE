package boaz.site.boazback.recruitment.application;


import boaz.site.boazback.common.exception.RecruitmentException;
import boaz.site.boazback.recruitment.domain.Recruitment;
import boaz.site.boazback.recruitment.dto.RecruitmentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RecruitmentServiceTest {

    private RecruitmentService recruitmentService;

    @Mock
    private RecruitmentRepository recruitmentRepository;
    private Recruitment recruitmentFake;
    @BeforeEach
    void setUp() {
        recruitmentService = new RecruitmentServiceImpl(recruitmentRepository);
        recruitmentFake = Recruitment.builder()
                .id(1L)
                .notificationUrl("1")
                .recruitmentUrl("2")
                .faqUrl("3")
                .isRecruitmentDuration(true)
                .build();
    }


    @Test
    void recruitment조회하기() {
        given(recruitmentRepository.findById(anyLong())).willReturn(Optional.of(recruitmentFake));
       RecruitmentDto.RecruitmentResponse recruitmentResponse = recruitmentService.getRecruitment();
        assertThat(recruitmentResponse.getRecruitmentUrl()).isEqualTo("2");
        assertThat(recruitmentResponse.isRecruitmentDuration()).isTrue();
    }

    @Test
    void recruitment조회실패(){
        given(recruitmentRepository.findById(anyLong())).willReturn(Optional.empty());
        Throwable error = assertThrows(RecruitmentException.class, () -> recruitmentService.getRecruitment());
        assertThat(error.getMessage()).isEqualTo("recruitment data not found");
    }
}
