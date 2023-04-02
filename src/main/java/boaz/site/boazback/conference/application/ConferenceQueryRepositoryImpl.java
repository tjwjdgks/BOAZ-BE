package boaz.site.boazback.conference.application;

import boaz.site.boazback.conference.domain.Conference;
import boaz.site.boazback.conference.domain.QConference;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ConferenceQueryRepositoryImpl implements ConferenceQueryRepository{

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * @param pageable
     * @param data
     * @return 제목 혹은 유저명에 특정 글자가 포함된 Pagination된 Conference Data 반환
     */
    @Override
    public Page<Conference> findConferenceByTitleAndWriterOrderByCreatedDateDesc(Pageable pageable, String data) {
        log.info("findConferenceByTitleAndWriterOrderByCreatedDateDesc start");
        QConference conference = QConference.conference;
        JPQLQuery<Conference> query = jpaQueryFactory.selectFrom(conference)
                .where(
                        ((conference.title.contains(data)).or(conference.editor.name.contains(data))).and(conference.published.eq(true))
                )
                .orderBy(conference.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        QueryResults<Conference> queryResults = query.fetchResults();
        Page<Conference> conferencePage = new PageImpl<Conference>(queryResults.getResults(), pageable, queryResults.getTotal());
        log.info("findConferenceByTitleAndWriterOrderByCreatedDateDesc end");
        return conferencePage;
    }
}
