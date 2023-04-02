package boaz.site.boazback.curriculum.api;

import boaz.site.boazback.common.domain.Result;
import boaz.site.boazback.curriculum.dto.CurriculumDto;
import boaz.site.boazback.curriculum.dto.MemberSuggestionDto;
import boaz.site.boazback.curriculum.dto.SkillDto;
import boaz.site.boazback.curriculum.service.CurriculumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/curriculum")
@RequiredArgsConstructor
public class CurriculumController {

    private final CurriculumService curriculumService;

    @GetMapping("/skills/{trackId}")
    public Result getSkillList(@PathVariable("trackId") int trackId) {
        log.info("getSkillList start");
        Result result = new Result().resultSuccess();
        List<SkillDto> skillDtoList = curriculumService.getSkillListByTrackId(trackId);
        result.setData(skillDtoList);
        log.info("getSkillList end");
        return result;
    }

    @GetMapping("/suggestion/{trackId}")
    public Result getCurriculumSuggestionList(@PageableDefault(page = 0,size = 40,sort = "id", direction = Sort.Direction.DESC) Pageable pageable,@PathVariable("trackId")int trackId) {
        log.info("getCurriculumSuggestionList start");
        Result result = new Result().resultSuccess();
        List<MemberSuggestionDto> memberSuggestionDtoList = curriculumService.getCurriculumSuggestionList(pageable,trackId);
        result.setData(memberSuggestionDtoList);
        log.info("getCurriculumSuggestionList end");
        return result;
    }

    @GetMapping("/{trackId}")
    public Result getCurriculumListByTrackId(@PathVariable("trackId") int trackId) {
        log.info("getCurriculumListByTrackId start");
        Result result = new Result().resultSuccess();
        CurriculumDto curriculumDtoList = curriculumService.getCurriculumByTrackId(trackId);
        result.setData(curriculumDtoList);
        log.info("getCurriculumListByTrackId end");
        return result;
    }
    @GetMapping
    public Result getCurriculumList() {
        log.info("getCurriculumList start");
        Result result = new Result().resultSuccess();
        List<CurriculumDto> curriculumDtoList = curriculumService.getCurriculumList();
        result.setData(curriculumDtoList);
        log.info("getCurriculumList end");
        return result;
    }
}
