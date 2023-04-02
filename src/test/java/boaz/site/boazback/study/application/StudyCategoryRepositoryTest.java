package boaz.site.boazback.study.application;

import boaz.site.boazback.study.domain.StudyCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class StudyCategoryRepositoryTest {


    @Autowired
    private StudyCategoryRepository studyCategoryRepository;


    @Test
    @Sql("classpath:sql/studycategory.sql")
    void 카테고리불러오기() {
        Optional<StudyCategory> studyCategory = studyCategoryRepository.findById(1L);
        assertThat(studyCategory).isNotEmpty();
        assertThat(studyCategory.get().getTitle()).isEqualTo("전체");
    }
}
