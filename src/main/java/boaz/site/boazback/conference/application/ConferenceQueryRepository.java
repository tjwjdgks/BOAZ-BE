package boaz.site.boazback.conference.application;

import boaz.site.boazback.conference.domain.Conference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConferenceQueryRepository {
    Page<Conference> findConferenceByTitleAndWriterOrderByCreatedDateDesc(Pageable pageable,String data);
}
