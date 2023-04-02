package boaz.site.boazback.study.application;

import boaz.site.boazback.study.domain.StudyCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyCategoryRepository extends JpaRepository<StudyCategory,Long> {
}
