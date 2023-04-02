package boaz.site.boazback.curriculum.service;

import boaz.site.boazback.BaseDataJpaTest;
import boaz.site.boazback.curriculum.domain.Curriculum;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CurriculumRepositoryTest extends BaseDataJpaTest {

    @Test
    void findByTrackId() {
        Curriculum byTrackId = curriculumRepository.findByTrackId(1);
        assertThat(byTrackId).isNotNull();
        assertThat(byTrackId.getTrackId()).isEqualTo(1);
    }

}