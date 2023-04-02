package boaz.site.boazback.curriculum.service;

import boaz.site.boazback.curriculum.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill,Long> {

    List<Skill> findByTrackId(int trackId);

}
