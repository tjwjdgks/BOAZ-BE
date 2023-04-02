package boaz.site.boazback.intro.application;


import boaz.site.boazback.intro.domain.IntroSubComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IntroSubCommentRepository extends JpaRepository<IntroSubComment,Long> {
    List<IntroSubComment> findByIntroId(Long id);
}
