package boaz.site.boazback.intro.application;

import boaz.site.boazback.BaseDataJpaTest;
import boaz.site.boazback.common.domain.JwtInfo;
import boaz.site.boazback.intro.dto.IntroInfo;
import boaz.site.boazback.user.application.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class IntroServiceSliceTest extends BaseDataJpaTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IntroRepository introRepository;

    private ObjectMapper objectMapper;

    private IntroService introService;

    private JwtInfo jwtInfo;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        introService = new IntroServiceImpl(userRepository,introRepository,objectMapper);
        jwtInfo =  new JwtInfo(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"),2,"s","s","s","user1",1);
    }

    @Test
    void intro_save() {
        Map<String, Object> user = new HashMap<>();
        user.put("id", "df8b21f0-c2d6-11ec-a6d6-0800200c9a66");
        IntroInfo result = introService.saveIntro(jwtInfo, "ttt1");
        System.out.println("result = " + result);
        assertThat(result.getContent()).isEqualTo("ttt1");
    }

    @Test
    void getIntroList(){
        Pageable pageable = PageRequest.of(0, 2);
        List<IntroInfo> result = introService.getIntroList(pageable);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSubComments().get(1).isErase()).isTrue();
        assertThat(result.get(0).getSubComments().get(1).getContents()).isEmpty();
    }


}
