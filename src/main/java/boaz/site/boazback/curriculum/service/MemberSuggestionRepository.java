package boaz.site.boazback.curriculum.service;

import boaz.site.boazback.curriculum.domain.MemberSuggestion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberSuggestionRepository extends JpaRepository<MemberSuggestion,Long> {
//    List<MemberSuggestion> findByOrderByIdDesc(Pageable pageable);

    List<MemberSuggestion> findByTrackIdOrderByIdDesc(int trackId, Pageable pageable);

}
