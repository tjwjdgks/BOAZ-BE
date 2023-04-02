package boaz.site.boazback;

import boaz.site.boazback.comment.application.repository.CommentRepository;
import boaz.site.boazback.comment.application.repository.SubCommentRepository;
import boaz.site.boazback.conference.application.ConferenceRepository;
import boaz.site.boazback.curriculum.service.CurriculumRepository;
import boaz.site.boazback.curriculum.service.SkillRepository;
import boaz.site.boazback.intro.application.IntroRepository;
import boaz.site.boazback.intro.application.IntroSubCommentRepository;
import boaz.site.boazback.question.application.QuestionRepository;
import boaz.site.boazback.review.application.ReviewRepository;
import boaz.site.boazback.study.application.StudyRepository;
import boaz.site.boazback.user.application.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
@Sql({
        "classpath:sql/question.sql",
        "classpath:sql/member.sql",
        "classpath:sql/studycategory.sql",
        "classpath:sql/conference.sql",
        "classpath:sql/study.sql",
        "classpath:sql/comment.sql",
        "classpath:sql/comment_sub.sql",
        "classpath:sql/intro.sql",
        "classpath:sql/intro_sub.sql",
//        "classpath:sql/recruitment.sql",
        "classpath:sql/review.sql",
        "classpath:sql/curriculum.sql",
})
public abstract class BaseDataJpaTest {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected StudyRepository studyRepository;

    @Autowired
    protected QuestionRepository questionRepository;

    @Autowired
    protected IntroRepository introRepository;

    @Autowired
    protected IntroSubCommentRepository introSubCommentRepository;

    @Autowired
    protected ConferenceRepository conferenceRepository;

    @Autowired
    protected CommentRepository commentRepository;

    @Autowired
    protected SubCommentRepository subCommentRepository;

    @Autowired
    protected ReviewRepository reviewRepository;

    @Autowired
    protected SkillRepository skillRepository;

//    @Autowired
//    protected RecruitmentRepository recruitmentRepository;

    @Autowired
    protected CurriculumRepository curriculumRepository;

    @Autowired
    protected EntityManager em;

    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(em);
    }
}
