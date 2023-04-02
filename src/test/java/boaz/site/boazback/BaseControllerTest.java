package boaz.site.boazback;

import boaz.site.boazback.admin.user.application.AdminUserService;
import boaz.site.boazback.admin.site.api.AdminSiteController;
import boaz.site.boazback.admin.site.application.AdminSiteService;
import boaz.site.boazback.admin.user.controller.AdminUserController;
import boaz.site.boazback.comment.api.CommentRestController;
import boaz.site.boazback.comment.api.CommentSubRestController;
import boaz.site.boazback.comment.application.service.CommentService;
import boaz.site.boazback.comment.application.service.SubCommentService;
import boaz.site.boazback.common.aop.JwtAspect;
import boaz.site.boazback.common.api.AuthController;
import boaz.site.boazback.common.application.JwtService;
import boaz.site.boazback.common.domain.JwtInfo;
import boaz.site.boazback.common.domain.TokenType;
import boaz.site.boazback.common.exception.ControllerExceptionHandler;
import boaz.site.boazback.common.resolver.JwtLoginArgumentResolver;
import boaz.site.boazback.common.storage.FileController;
import boaz.site.boazback.common.storage.ObjectStorageUtil;
import boaz.site.boazback.common.util.CookieUtil;
import boaz.site.boazback.common.util.HttpReqResUtils;
import boaz.site.boazback.common.util.JwtUtil;
import boaz.site.boazback.common.util.SlackUtil;
import boaz.site.boazback.conference.api.ConferenceController;
import boaz.site.boazback.conference.application.ConferenceQueryRepository;
import boaz.site.boazback.conference.application.ConferenceService;
import boaz.site.boazback.curriculum.api.CurriculumController;
import boaz.site.boazback.curriculum.service.CurriculumService;
import boaz.site.boazback.intro.application.IntroService;
import boaz.site.boazback.intro.application.IntroSubCommentService;
import boaz.site.boazback.intro.controller.IntroRestController;
import boaz.site.boazback.intro.controller.IntroSubRestController;
import boaz.site.boazback.question.api.QuestionController;
import boaz.site.boazback.question.application.QuestionService;
import boaz.site.boazback.recruitment.api.RecruitmentController;
import boaz.site.boazback.recruitment.application.RecruitmentService;
import boaz.site.boazback.review.api.ReviewController;
import boaz.site.boazback.review.application.ReviewService;
import boaz.site.boazback.study.api.StudyController;
import boaz.site.boazback.study.application.StudyService;
import boaz.site.boazback.user.api.UserController;
import boaz.site.boazback.user.application.UserService;
import boaz.site.boazback.user.domain.Role;
import boaz.site.boazback.user.domain.User;
import boaz.site.boazback.user.dto.RedirectionConfig;
import boaz.site.boazback.user.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.http.Cookie;

import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@WebMvcTest({
        ConferenceController.class,
        StudyController.class,
        UserController.class,
        CommentRestController.class,
        CommentSubRestController.class,
        IntroRestController.class,
        IntroSubRestController.class,
        QuestionController.class,
        FileController.class,
        AuthController.class,
        ControllerExceptionHandler.class,
        AdminUserController.class,
        AdminSiteController.class,
        RecruitmentController.class,
        ReviewController.class,
        CurriculumController.class
})
@Import({JwtAspect.class,JwtService.class, JwtUtil.class, CookieUtil.class, JwtLoginArgumentResolver.class})
public abstract class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected WebApplicationContext ctx;

    @Autowired
    protected JwtAspect jwtAspect;

    @Autowired
    protected JwtService jwtService;

    @Autowired
    protected JwtUtil jwtUtil;

    @Autowired
    protected CookieUtil cookieUtil;

    @MockBean
    protected DatabaseReader databaseReader;

    @Autowired
    protected JwtLoginArgumentResolver jwtLoginArgumentResolver;

    @MockBean
    protected ConferenceService conferenceService;

    @MockBean
    protected StudyService studyService;

    @MockBean
    protected UserService userService;

    @MockBean
    protected QuestionService questionService;

    @MockBean
    protected CommentService commentService;

    @MockBean
    protected IntroService introService;

    @MockBean
    protected IntroSubCommentService introSubCommentService;

    @MockBean
    protected SubCommentService subCommentService;

    @MockBean
    protected ObjectStorageUtil objectStorageUtil;

    @MockBean
    protected RedirectionConfig redirectionConfig;

    @MockBean
    protected HttpReqResUtils httpReqResUtils;

    @MockBean
    protected ConferenceQueryRepository conferenceQueryRepository;

    @MockBean
    protected AdminUserService adminUserService;

    @MockBean
    protected AdminSiteService adminSiteService;

    @MockBean
    protected RecruitmentService recruitmentService;

    @MockBean
    protected ReviewService reviewService;

    @MockBean
    protected SlackUtil slackUtil;

    @MockBean
    protected CurriculumService curriculumService;


    protected JwtInfo jwtInfo;

    protected String accessTokenStr;
    protected String refreshTokenStr;
    protected String certificateTokenStr;

    protected Cookie accessToken;
    protected Cookie refreshToken;
    protected Cookie certificateToken;

    protected Country korea;

    protected UserDto userDto;

    protected CountryResponse countryResponse;

    protected void baseSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .alwaysDo(print())
                .build();
        setUpUser();
        setUpToken();
    }

    protected void setUpUser(){
        userDto = UserDto.builder()
                .email("example@naver.com")
                .password("")
                .name("bob")
                .section(3)
                .year("16")
                .authenticationCode("")
                .birthDate("0000-00-00")
                .build();
    }

    protected void setUpToken(){
        User build = User.builder()
                .id(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"))
                .email("example@naver.com")
                .password("hello01")
                .name("bob")
                .section(3)
                .year("16")
                .birthDate("0000-00-00")
                .build();
        build.registRole(Role.ADMIN);
        jwtInfo = new JwtInfo(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"), 1, "bob", "example@example.com", "16", "16_analysis_bob", 0);
        accessTokenStr = jwtService.getToken(TokenType.ACCESS, build);
        refreshTokenStr = jwtService.getToken(TokenType.REFRESH, build);
        certificateTokenStr = jwtService.getToken("example@naver.com");

        accessToken = new Cookie("accessToken", accessTokenStr);
        refreshToken = new Cookie("refreshToken", refreshTokenStr);
        certificateToken = new Cookie("certificateToken", certificateTokenStr);





    }

}
