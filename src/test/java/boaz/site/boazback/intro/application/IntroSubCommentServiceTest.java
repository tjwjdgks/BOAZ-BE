package boaz.site.boazback.intro.application;

import boaz.site.boazback.common.domain.JwtInfo;
import boaz.site.boazback.common.exception.AuthorityException;
import boaz.site.boazback.common.exception.UserException;
import boaz.site.boazback.intro.domain.Intro;
import boaz.site.boazback.intro.domain.IntroSubComment;
import boaz.site.boazback.intro.dto.IntroSubCommentInfo;
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
class IntroSubCommentServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private IntroRepository introRepository;
    @Mock
    private IntroSubCommentRepository introSubCommentRepository;

    private IntroSubCommentService introSubCommentService;
    Intro intro;
    User user;
    private JwtInfo data;
    @BeforeEach
    void setUp() {
        data = new JwtInfo(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"),2,"s","s","s","user1",1);
        ObjectMapper objectMapper = new ObjectMapper();
        introSubCommentService = new IntroSubCommentServiceImpl(userRepository, introRepository, introSubCommentRepository, objectMapper);
    }

    @Test
    void saveSubComment_user_empty() {
        given(userRepository.findById(any())).willReturn(Optional.empty());
        Throwable error = assertThrows(UserException.class, () -> introSubCommentService.saveSubComment(data, "", 1L));
        assertThat(error.getMessage()).isEqualTo("user not found");
    }

    @Test
    void saveSubComment_intro_empty() {
        User user = new User(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"),1,"s","s","s","s","s");
        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(introRepository.findById(anyLong())).willReturn(Optional.empty());
        Throwable error = assertThrows(IllegalStateException.class, () -> introSubCommentService.saveSubComment(data, "", 1L));
        assertThat(error.getMessage()).isEqualTo("intro not found");
    }

    @Test
    void updateSubComment() {
        User user = new User(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"),1,"s","s","s","s","s");
         intro = new Intro(1L,"sd",user,new ArrayList<>(),false);
        IntroSubComment introSubComment = new IntroSubComment(1L,"das",intro,user,false);
        given(introSubCommentRepository.findById(anyLong())).willReturn(Optional.of(introSubComment));
        given(introSubCommentRepository.save(any())).willReturn(introSubComment.eraseSubComment());
        IntroSubCommentInfo result = introSubCommentService.deleteSubComment(data, 1L);
        assertThat(result.isErase()).isTrue();
    }

    @Test
    void deleteSubComment() {
        User user = new User(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"),1,"s","s","s","s","s");
         intro = new Intro(1L,"sd",user,new ArrayList<>(),false);
        IntroSubComment introSubComment = new IntroSubComment(1L,"das",intro,user,false);
        given(introSubCommentRepository.findById(anyLong())).willReturn(Optional.of(introSubComment));
        given(introSubCommentRepository.save(any())).willReturn(introSubComment.updateSubComment("test"));
        IntroSubCommentInfo result = introSubCommentService.deleteSubComment(data, 1L);
        assertThat(result.getContents()).isEqualTo("test");
    }

    @Test
    void not_introsubcomment(){
        given(introSubCommentRepository.findById(anyLong())).willReturn(Optional.empty());
        Throwable error = assertThrows(IllegalStateException.class, () -> introSubCommentService.deleteSubComment(data, 1L));
        assertThat(error.getMessage()).isEqualTo("intro subComment not found");
    }

    @Test
    void checkWriteAuthority(){
        data.setId(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a67"));
        User user = new User(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"),1,"s","s","s","s","s");
        intro = new Intro(1L,"sd",user,new ArrayList<>(),false);
        IntroSubComment introSubComment = new IntroSubComment(1L,"das",intro, user,false);
        given(introSubCommentRepository.findById(anyLong())).willReturn(Optional.of(introSubComment));
        Throwable error = assertThrows(AuthorityException.class, () -> introSubCommentService.deleteSubComment(data, 1L));
        assertThat(error.getMessage()).isEqualTo("you are not admin or Editor");
    }


}
