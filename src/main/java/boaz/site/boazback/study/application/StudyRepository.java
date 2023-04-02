package boaz.site.boazback.study.application;

import boaz.site.boazback.study.domain.Study;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyRepository extends JpaRepository<Study,Long> {
    List<Study> findByStudyCategory_IdAndPublishedIsTrueOrderByModifiedDateDesc(Long id, Pageable pageable);
    List<Study> findByPublishedIsTrueOrderByModifiedDateDesc(Pageable pageable);

    @Query(nativeQuery = true,  value = "select count(id) from study where study_category_id =?1 and published=true")
    Long countTotalStudy(Long studyId);

    @Query(nativeQuery = true,  value = "select count(id) from study where published = true")
    Long countTotalStudy();




}
