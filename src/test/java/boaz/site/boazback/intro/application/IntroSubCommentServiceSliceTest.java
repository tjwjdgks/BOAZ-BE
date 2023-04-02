package boaz.site.boazback.intro.application;

import boaz.site.boazback.common.domain.JwtInfo;
import boaz.site.boazback.intro.dto.IntroSubCommentInfo;
import boaz.site.boazback.user.application.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Sql({"classpath:sql/studycategory.sql","classpath:sql/member.sql","classpath:sql/study.sql","classpath:sql/intro.sql","classpath:sql/intro_sub.sql"})
class IntroSubCommentServiceSliceTest {


    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  IntroRepository introRepository;
    @Autowired
    private  IntroSubCommentRepository introSubCommentRepository;

    private IntroSubCommentService introSubCommentService;

    private JwtInfo jwtInfo;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        introSubCommentService = new IntroSubCommentServiceImpl(userRepository, introRepository, introSubCommentRepository, objectMapper);
    }

    @Test
    void saveSubComment() {
        jwtInfo = new JwtInfo();
        jwtInfo.setId(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"));
        IntroSubCommentInfo result = introSubCommentService.saveSubComment(jwtInfo, "data", 1L);
        assertThat(result.getContents()).isEqualTo("data");
        assertThat(result.isErase()).isFalse();
    }

    @Test
    void getSubCommentsByIntro(){
        List<IntroSubCommentInfo> result = introSubCommentService.getSubCommentsByIntro(1L);
        assertThat(result).hasSize(2);
    }

}
