package boaz.site.boazback.curriculum.service;

import boaz.site.boazback.curriculum.domain.Curriculum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurriculumRepository extends JpaRepository<Curriculum, Long> {

   Curriculum findByTrackId(int trackId);
}
