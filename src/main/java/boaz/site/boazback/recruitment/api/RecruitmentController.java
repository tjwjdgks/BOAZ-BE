package boaz.site.boazback.recruitment.api;

import boaz.site.boazback.common.domain.Result;
import boaz.site.boazback.recruitment.application.RecruitmentService;
import boaz.site.boazback.recruitment.dto.RecruitmentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recruitment")
@Slf4j
@RequiredArgsConstructor
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    @GetMapping
    public Result getRecruitment(){
        log.info("getRecruitment api start");
        Result result = new Result().resultSuccess();
        RecruitmentDto.RecruitmentResponse recruitmentResponse = recruitmentService.getRecruitment();
        result.setData(recruitmentResponse);
        log.info("getRecruitment api end");
        return result;
    }
}
