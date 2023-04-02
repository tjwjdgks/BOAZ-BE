package boaz.site.boazback.curriculum.api;

import boaz.site.boazback.BaseControllerTest;
import boaz.site.boazback.curriculum.domain.MemberSuggestion;
import boaz.site.boazback.curriculum.domain.Skill;
import boaz.site.boazback.curriculum.dto.MemberSuggestionDto;
import boaz.site.boazback.curriculum.dto.SkillDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class CurriculumControllerTest extends BaseControllerTest {

    @BeforeEach
    void setUp() {
        baseSetUp();
    }

    @Test
    void getSkillList() throws Exception {
        List<Skill> skillList = new ArrayList<>();
        Skill skill = Skill.builder()
                .trackId(3)
                .logoUrl("")
                .name("spark")
                .build();
        Skill skill2 = Skill.builder()
                .trackId(2)
                .logoUrl("")
                .name("spark")
                .build();
        skillList.add(skill);
        skillList.add(skill2);
        List<SkillDto> skills = skillList.stream().filter( e -> e.getTrackId() == 1).map(SkillDto::new).collect(Collectors.toList());
        given(curriculumService.getSkillListByTrackId(anyInt())).willReturn(skills);
        mockMvc.perform(get("/curriculum/skills/{trackId}",1))
                .andDo(print())
                .andExpect(jsonPath("$.payload.length()").value(0));
    }

    @Test
    void getCurriculumSuggestionList() throws Exception {
        MemberSuggestion memberSuggestion = MemberSuggestion.builder()
                .track("분석")
                .build();
        MemberSuggestionDto memberSuggestionDto = new MemberSuggestionDto(memberSuggestion);
        List<MemberSuggestionDto> memberSuggestions = new ArrayList<>();
        memberSuggestions.add(memberSuggestionDto);
        given(curriculumService.getCurriculumSuggestionList(any(), anyInt())).willReturn(memberSuggestions);
        mockMvc.perform(get("/curriculum/suggestion/{trackId}",1))
                .andDo(print())
                .andExpect(jsonPath("$.payload.length()").value(1));
    }

}
