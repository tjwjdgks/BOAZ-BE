package boaz.site.boazback.comment.application;

import boaz.site.boazback.comment.application.repository.CommentRepository;
import boaz.site.boazback.comment.application.service.CommentService;
import boaz.site.boazback.comment.domain.Comment;
import boaz.site.boazback.comment.domain.SubComment;
import boaz.site.boazback.comment.dto.CommentDto;
import boaz.site.boazback.common.domain.JwtInfo;
import boaz.site.boazback.study.application.StudyRepository;
import boaz.site.boazback.study.domain.Study;
import boaz.site.boazback.user.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private StudyRepository studyRepository;

    CommentService commentService;


    private JwtInfo jwtInfo;
    private Comment comment1;
    private Page<Comment> commentPageable;
    private CommentDto commentDto1;
    private Study study;
    private User fakeUser;
    @BeforeEach
    void setUp(){
        jwtInfo = new JwtInfo(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"),2,"s","s","s","user1",1);
        fakeUser = User.builder()
                .email("example@naver.com")
                .password("hello01")
                .name("bob")
                .section(3)
                .year("16")
                .birthDate("0000-00-00")
                .id(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"))
                .build();
        study = Study.builder()
                .id(1L)
                .build();
        comment1 = Comment.builder()
                .id(0L)
                .commentText("test")
                .subComments(new ArrayList<>())
                .editName("user1")
                .erase(false)
                .study(study)
                .user(fakeUser)
                .build();
        commentDto1 = comment1.toDto();
        commentPageable = new PageImpl<Comment>(List.of(comment1), PageRequest.of(0,1),1);
        commentService = new CommentServiceImpl(commentRepository,studyRepository);
    }

    @Test
    public void insertData(){
        given(studyRepository.findById(anyLong())).willReturn(Optional.of(study));
        given(commentRepository.save(any())).willReturn(comment1);
        CommentDto commentDto = commentService.insertData(fakeUser,1L,"test",jwtInfo);
        assertEquals(commentDto.getCommentId(),comment1.getId());
    }

    @Test
    public void getData(){
        given(commentRepository.findCommentWithStudy(any(),any())).willReturn(commentPageable);
        Page<CommentDto> data = commentService.getData(1L, PageRequest.of(0, 1));
        assertEquals(data.getContent().size(),1);
        assertEquals(data.getContent().get(0).getCommentId(),commentPageable.getContent().get(0).getId());
    }
    @Test
    public void updateData(){
        given(commentRepository.findById(any())).willReturn(Optional.of(comment1));
        CommentDto commentDto = commentService.updateData(0l, "test", jwtInfo);
        assertEquals(commentDto.getCommentText(),"test");
    }
    @Test
    public void deleteData(){
        given(commentRepository.findById(any())).willReturn(Optional.of(comment1));
        commentService.deleteData(0l, jwtInfo);
        assertTrue(comment1.isErase());
        CommentDto commentDto = comment1.toDto();
        assertEquals(commentDto.getCommentText(),"");
    }

    @Test
    @DisplayName("comment 없는 경우")
    public void fail(){
        given(commentRepository.findById(any())).willReturn(Optional.empty());
        IllegalStateException test = assertThrows(IllegalStateException.class,
                () -> commentService.updateData(0l, "test", jwtInfo));
        assertEquals(test.getMessage(),"comment not exist");
    }
    @Test
    @DisplayName("사용자 다른 경우")
    public void fail2(){
        JwtInfo jwtDiff = new JwtInfo(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a67"),2,"s","s","s","diff",1);
        given(commentRepository.findById(any())).willReturn(Optional.of(comment1));
        IllegalStateException test = assertThrows(IllegalStateException.class, () -> commentService.deleteData(0l, jwtDiff));
        assertEquals(test.getMessage(),"can't change,user is different");
    }
}
