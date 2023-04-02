package boaz.site.boazback.intro.application;

import boaz.site.boazback.common.domain.JwtInfo;
import boaz.site.boazback.intro.domain.Intro;
import boaz.site.boazback.intro.dto.IntroInfo;
import boaz.site.boazback.user.application.UserRepository;
import boaz.site.boazback.user.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class IntroServiceTest {

    @Mock
    private IntroRepository introRepository;
    @Mock
    private UserRepository userRepository;

    private ObjectMapper objectMapper;

    private IntroService introService;
    private Intro intro;
    private JwtInfo data;

    @BeforeEach
    void setUp() {
        data = new JwtInfo(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"),2,"s","s","s","user1",1);
        User user = new User(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"),1,"s","s","s","s","s");
        intro = new Intro(1L,"ttt",user,new ArrayList<>(),false);
        objectMapper = new ObjectMapper();
        introService = new IntroServiceImpl(userRepository,introRepository,objectMapper);
    }

    @Test
    void updateComment() {
        JwtInfo jwtInfo = new JwtInfo();
        jwtInfo.setId(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"));
        given(introRepository.findById(anyLong())).willReturn(Optional.of(intro));
        given(introRepository.save(any())).willReturn(intro.updateContent("test"));
        IntroInfo result = introService.updateComment(jwtInfo,1L,"test");
        assertThat(result.getContent()).isEqualTo("test");
    }

    @Test
    void deleteComment() {
        given(introRepository.findById(anyLong())).willReturn(Optional.of(intro));
        given(introRepository.save(any())).willReturn(intro.eraseIntro());
        IntroInfo result = introService.deleteComment(data,1L);
        assertThat(result.isErase()).isTrue();
    }

    @Test
    void checkWriteAuthority(){
        data.setId(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a67"));
        given(introRepository.findById(anyLong())).willReturn(Optional.of(intro));
        Throwable error = assertThrows(IllegalStateException.class, () ->
            introService.deleteComment(data, 1L));
        assertThat(error.getMessage()).isEqualTo("not same user");
    }

    @Test
    void intro_not_exist(){
        given(introRepository.findById(anyLong())).willReturn(Optional.empty());
        Throwable error = assertThrows(IllegalStateException.class, () ->
                introService.deleteComment(data, 1L));
        assertThat(error.getMessage()).isEqualTo("intro not exist");
    }


}
