package boaz.site.boazback.curriculum.service;

import boaz.site.boazback.curriculum.domain.Curriculum;
import boaz.site.boazback.curriculum.domain.MemberSuggestion;
import boaz.site.boazback.curriculum.domain.Skill;
import boaz.site.boazback.curriculum.dto.CurriculumDto;
import boaz.site.boazback.curriculum.dto.MemberSuggestionDto;
import boaz.site.boazback.curriculum.dto.SkillDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CurriculumServiceTest {


    private CurriculumService curriculumService;

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private MemberSuggestionRepository memberSuggestionRepository;

    @Mock
    private CurriculumRepository curriculumRepository;

    @BeforeEach
    void setUp() {
        curriculumService = new CurriculumServiceImpl(skillRepository, memberSuggestionRepository, curriculumRepository);
    }

    @Test
    void GetSkillListByTrackId(){
        int trackId = 1;
        //given
        Skill skill = Skill.builder()
                .name("spark")
                .trackId(3)
                .logoUrl("")
                .build();
        List<Skill> skills = new ArrayList<>();
        skills.add(skill);
        given(skillRepository.findByTrackId(anyInt())).willReturn(skills);
        //when
        List<SkillDto> skillList= curriculumService.getSkillListByTrackId(trackId);
        //then
        assertThat(skillList).hasSize(1);
        assertThat(skillList.get(0).getName()).isEqualTo("spark");
    }

    @Test
    void getCurriculumSuggestionList() {
        List<MemberSuggestion> memberSuggestions = new ArrayList<>();
        given(memberSuggestionRepository.findByTrackIdOrderByIdDesc(anyInt(),any())).willReturn(memberSuggestions);
        Pageable pageable = PageRequest.of(0, 1);
        List<MemberSuggestionDto> memberSuggestionDtos =curriculumService.getCurriculumSuggestionList(pageable,1);
        assertThat(memberSuggestionDtos).hasSize(0);
    }

    @Test
    void getCurriculumListByTrackId() {
        Curriculum curriculum1 = Curriculum.builder()
                .title("나만의 인공지능을 만들어봐요")
                .trackId(2)
                .build();
        Curriculum curriculum2 = Curriculum.builder()
                .title("데이터로 스토리텔링해")
                .trackId(1)
                .build();
        List<Curriculum> curriculums = List.of(curriculum1,curriculum2);
        given(curriculumRepository.findAll()).willReturn(curriculums);
        List<CurriculumDto> curriculumDtos = curriculumService.getCurriculumList();
        assertThat(curriculumDtos).hasSize(2);
        assertThat(curriculumDtos.get(0).getTitle()).isEqualTo("데이터로 스토리텔링해");
    }
}
