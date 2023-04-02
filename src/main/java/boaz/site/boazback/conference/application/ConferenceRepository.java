package boaz.site.boazback.conference.application;

import boaz.site.boazback.conference.domain.Conference;
import boaz.site.boazback.conference.dto.ConferenceInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference,Long> {
    Page<Conference> findByOrderByCreatedDateDesc(Pageable pageable);

    Page<ConferenceInfo> findByTitleContainsAndPublishedOrderByCreatedDateDesc(String title, boolean published, Pageable pageable);

    Page<Conference> findByPublishedOrderByCreatedDateDesc(boolean published, Pageable pageable);

    Page<ConferenceInfo> findByTitleContainsOrderByCreatedDateDesc(String title, Pageable pageable);

    Optional<Conference> findByIdAndPublished(Long id, boolean published);

    List<Conference> findByPublished(boolean published);


}
