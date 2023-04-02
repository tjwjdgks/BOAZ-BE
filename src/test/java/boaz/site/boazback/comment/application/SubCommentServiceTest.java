package boaz.site.boazback.comment.application;

import boaz.site.boazback.comment.application.repository.CommentRepository;
import boaz.site.boazback.comment.application.repository.SubCommentRepository;
import boaz.site.boazback.comment.application.service.SubCommentService;
import boaz.site.boazback.comment.domain.Comment;
import boaz.site.boazback.comment.domain.SubComment;
import boaz.site.boazback.comment.dto.SubCommentDto;
import boaz.site.boazback.common.domain.JwtInfo;
import boaz.site.boazback.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SubCommentServiceTest {

    @Mock
    private SubCommentRepository subCommentRepository;
    @Mock
    private CommentRepository commentRepository;

    SubCommentService subCommentService;
    private JwtInfo jwtInfo;
    private SubComment subComment;
    private Comment comment;
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

        comment = Comment.builder()
                .id(0L)
                .commentText("test")
                .subComments(new ArrayList<>())
                .editName("user1")
                .user(fakeUser)
                .erase(false)
                .build();

        subComment = SubComment.builder()
                .subCommentText("subtest")
                .erase(false)
                .editName("user1")
                .comment(comment)
                .user(fakeUser)
                .build();
        subCommentService = new SubCommentServiceImpl(subCommentRepository,commentRepository);
    }

    @Test
    public void getData(){
        given(subCommentRepository.findByComment_Id(anyLong())).willReturn(List.of(subComment));
        List<SubCommentDto> subCommentDtos = subCommentService.getSubCommentsByCommentId(1L);
        assertEquals(subCommentDtos.size(),1);
        assertEquals(subCommentDtos.get(0).getCommentId(),subComment.getComment().getId());
    }

    @Test
    public void saveData(){
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));
        given(subCommentRepository.save(any())).willReturn(subComment);
        SubCommentDto subCommentDto = subCommentService.saveSubComment(fakeUser,1L, "subtest", jwtInfo);
        assertEquals(subCommentDto.getSubCommentText(),subComment.getSubCommentText());
        assertEquals(subCommentDto.getSubCommentEditName(),subComment.getEditName());
        assertEquals(subCommentDto.getCommentId(),comment.getId());
    }
    @Test
    public void save_fail_no_comment(){
        given(commentRepository.findById(anyLong())).willReturn(Optional.empty());
        IllegalStateException test = assertThrows(IllegalStateException.class,
                () -> subCommentService.saveSubComment(fakeUser,1L,"subtest",jwtInfo));

        assertEquals(test.getMessage(),"comment not found");
    }
    @Test
    public void updateData(){
        given(subCommentRepository.findById(anyLong())).willReturn(Optional.of(subComment));
        SubCommentDto subCommentDto = subCommentService.updateSubComment(1L, "subChange", jwtInfo);
        assertEquals(subCommentDto.getSubCommentText(),"subChange");
    }
    @Test
    public void deleteData(){
        given(subCommentRepository.findById(anyLong())).willReturn(Optional.of(subComment));
        Long deleteSubComment = subCommentService.deleteSubComment(1L, jwtInfo);
        assertTrue(subComment.isErase());
        SubCommentDto subCommentDto = subComment.toSubCommentDto();
        assertEquals(subCommentDto.getSubCommentText(),"");
    }
    @Test
    @DisplayName("sub comment 없는 경우")
    public void fail(){
        given(subCommentRepository.findById(anyLong())).willReturn(Optional.empty());
        IllegalStateException test = assertThrows(IllegalStateException.class,
                () -> subCommentService.updateSubComment(1L,"test",jwtInfo));
        assertEquals(test.getMessage(),"subComment not found");
    }
    @Test
    @DisplayName("사용자 다른 경우")
    public void fail2(){
        JwtInfo jwtDiff = new JwtInfo(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a67"),2,"s","s","s","diff",1);
        given(subCommentRepository.findById(anyLong())).willReturn(Optional.of(subComment));
        IllegalStateException test = assertThrows(IllegalStateException.class,
                () -> subCommentService.deleteSubComment(1L,jwtDiff));
        assertEquals(test.getMessage(),"can't change,user is different");

    }
}
