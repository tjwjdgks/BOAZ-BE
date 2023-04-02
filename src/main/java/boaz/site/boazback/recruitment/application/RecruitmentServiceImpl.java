package boaz.site.boazback.recruitment.application;

import boaz.site.boazback.common.exception.RecruitmentException;
import boaz.site.boazback.recruitment.domain.Recruitment;
import boaz.site.boazback.recruitment.dto.RecruitmentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecruitmentServiceImpl implements RecruitmentService {

    private final RecruitmentRepository recruitmentRepository;

    @Override
    public RecruitmentDto.RecruitmentResponse getRecruitment() {
        log.info("getRecruitment service start");
        Optional<Recruitment> recruitment = recruitmentRepository.findById(1L);
        if (recruitment.isEmpty()) {
            log.error("recruitment empty");
            throw RecruitmentException.RECRUITMENT_NOT_FOUND;
        }
        RecruitmentDto.RecruitmentResponse recruitmentResponse = RecruitmentDto.RecruitmentResponse.translateEntityBuilder().recruitment(recruitment.get()).translate();
        log.info("getRecruitment service end");
        return recruitmentResponse;
    }
}
