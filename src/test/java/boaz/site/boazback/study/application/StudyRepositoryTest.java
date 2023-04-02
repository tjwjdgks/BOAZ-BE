package boaz.site.boazback.study.application;

import boaz.site.boazback.BaseDataJpaTest;
import boaz.site.boazback.comment.domain.Comment;
import boaz.site.boazback.study.domain.Study;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;



class StudyRepositoryTest extends BaseDataJpaTest {

    @Test
    void 카테고리에수정날짜내림차순(){
        Pageable pageable =  PageRequest.of(0, 2);
        List<Study> studyList = studyRepository.findByStudyCategory_IdAndPublishedIsTrueOrderByModifiedDateDesc(1L, pageable);
        System.out.println("studyList = " + studyList);
        assertThat(studyList).hasSize(2);
    }

    @Test
    void 수정날짜내림차순(){
        Pageable pageable = PageRequest.of(0, 3);
        List<Study> studyList = studyRepository.findByPublishedIsTrueOrderByModifiedDateDesc(pageable);
        System.out.println("studyList = " + studyList);
        assertThat(studyList).hasSize(3);
    }


    @Test
    void 데이터갯수세기(){
        Long a = studyRepository.countTotalStudy(1L);
        assertThat(a).isEqualTo(2);

        Long b = studyRepository.countTotalStudy();
        assertThat(b).isEqualTo(3);
    }

    @Test
    void 데이터삭제(){
        Optional<Study> byId = studyRepository.findById(1L);
        Study study = byId.get();
        List<Comment> comments = study.getComments();
        assertThat(comments.isEmpty()).isFalse();
        studyRepository.delete(byId.get());
        em.flush();
        em.clear();
        Optional<Study> byId2 = studyRepository.findById(1L);
        assertThat(byId2).isEmpty();
    }


}
