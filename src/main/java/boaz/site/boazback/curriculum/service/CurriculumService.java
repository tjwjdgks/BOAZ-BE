package boaz.site.boazback.curriculum.service;

import boaz.site.boazback.curriculum.dto.CurriculumDto;
import boaz.site.boazback.curriculum.dto.MemberSuggestionDto;
import boaz.site.boazback.curriculum.dto.SkillDto;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface CurriculumService {
    List<SkillDto> getSkillListByTrackId(int trackId);

    List<MemberSuggestionDto> getCurriculumSuggestionList(Pageable pageable, int trackId);

    CurriculumDto getCurriculumByTrackId(int trackId);

    List<CurriculumDto> getCurriculumList();

}
