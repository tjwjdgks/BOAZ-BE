package boaz.site.boazback.conference.application;

import boaz.site.boazback.BaseDataJpaTest;
import boaz.site.boazback.conference.domain.Conference;
import boaz.site.boazback.conference.domain.QConference;
import boaz.site.boazback.conference.dto.ConferenceInfo;
import boaz.site.boazback.user.domain.QUser;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


class ConferenceRepositoryTest extends BaseDataJpaTest {

    @Test
    void findByPublished() {
        List<Conference> result = conferenceRepository.findByPublished(true);
        assertThat(result).hasSize(1);
    }


    @Test
    void findByOrderByCreatedDateDesc() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Conference> result = conferenceRepository.findByOrderByCreatedDateDesc(pageable);
        System.out.println("result = " + result);
        assertThat(result).hasSize(2);
        assertThat(result.getContent().get(0).getId()).isEqualTo(3L);
    }

    @Test
    void findByOrderByCreatedDateDesc2() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Conference> result = conferenceRepository.findByOrderByCreatedDateDesc(pageable);
        List<ConferenceInfo> result2 = result.getContent().stream().map(ConferenceInfo::new).collect(Collectors.toList());
        Page<ConferenceInfo> data = new PageImpl<>(result2,pageable,result.getTotalElements());
        System.out.println("total size = " + data.getTotalElements());
        System.out.println("data.getTotalPages() = " + data.getTotalPages());
        System.out.println("data = " + data.getContent());
    }


    @Test
    void findByTitleContainsOrderByCreatedDateDesc(){
        Pageable pageable = PageRequest.of(0, 2);
        Page<ConferenceInfo> conferenceInfoPage = conferenceRepository.findByTitleContainsOrderByCreatedDateDesc("3", pageable);
        assertThat(conferenceInfoPage.getTotalElements()).isEqualTo(1);
        assertThat(conferenceInfoPage.getContent().get(0).getTitle()).isEqualTo("컨퍼런스3");
    }


    @Test
    void findByPublishedOrderByCreatedDateDesc() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Conference> result = conferenceRepository.findByPublishedOrderByCreatedDateDesc(true,pageable);
        System.out.println("result = " + result);
        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(3L);
    }

    @Test
    void searchConferenceByTitleAndWriter(){
        QConference conference = QConference.conference;
        Pageable pageable = PageRequest.of(0, 2);
        String input = "예진";
        List<Conference> data = jpaQueryFactory().selectFrom(conference)
                .where(
                        (conference.title.contains(input)).or(conference.editor.name.contains(input))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        assertThat(data).hasSize(2);
    }

}
