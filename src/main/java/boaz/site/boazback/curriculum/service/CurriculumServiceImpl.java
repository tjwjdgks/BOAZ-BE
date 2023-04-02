package boaz.site.boazback.curriculum.service;

import boaz.site.boazback.curriculum.domain.Curriculum;
import boaz.site.boazback.curriculum.domain.MemberSuggestion;
import boaz.site.boazback.curriculum.domain.Skill;
import boaz.site.boazback.curriculum.dto.CurriculumDto;
import boaz.site.boazback.curriculum.dto.MemberSuggestionDto;
import boaz.site.boazback.curriculum.dto.SkillDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurriculumServiceImpl implements CurriculumService {

    private final SkillRepository skillRepository;
    private final MemberSuggestionRepository memberSuggestionRepository;
    private final CurriculumRepository curriculumRepository;


    @Override
    public List<SkillDto> getSkillListByTrackId(int trackId) {
        log.info("getSkillListByTrackId service start");
        List<Skill> skills = skillRepository.findByTrackId(trackId);
        List<SkillDto> skillDtos = skills.stream().map(SkillDto::new).collect(Collectors.toList());
        log.info("getSkillListByTrackId service start");
        return skillDtos;
    }

    @Override
    public List<MemberSuggestionDto> getCurriculumSuggestionList(Pageable pageable, int trackId) {
        log.info("getCurriculumSuggestionList service start");
        List<MemberSuggestion> memberSuggestions = memberSuggestionRepository.findByTrackIdOrderByIdDesc(trackId,pageable);
        List<MemberSuggestionDto> memberSuggestionDtoList = memberSuggestions
                .stream()
                .map(MemberSuggestionDto::new)
                .collect(Collectors.toList());
        log.info("getCurriculumSuggestionList service end");
        return memberSuggestionDtoList;
    }

    @Override
    public CurriculumDto getCurriculumByTrackId(int trackId) {
        log.info("getCurriculumListByTrackId service start");
       Curriculum curriculum = curriculumRepository.findByTrackId(trackId);
       return new CurriculumDto(curriculum);
    }

    @Override
    public List<CurriculumDto> getCurriculumList() {
        log.info("getCurriculumList service start");
        List<Curriculum> curriculums = curriculumRepository.findAll();
        List<CurriculumDto> curriculumDtoList = curriculums
                .stream()
                .map(CurriculumDto::new)
                .sorted(Comparator.comparingInt(CurriculumDto::getTrackId))
                .collect(Collectors.toList());
        log.info("getCurriculumList service end");
        return curriculumDtoList;
    }
}
