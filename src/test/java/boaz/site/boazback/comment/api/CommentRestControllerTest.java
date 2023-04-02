package boaz.site.boazback.comment.api;

import boaz.site.boazback.BaseControllerTest;
import boaz.site.boazback.comment.domain.Comment;
import boaz.site.boazback.comment.domain.SubComment;
import boaz.site.boazback.comment.dto.CommentDto;
import boaz.site.boazback.comment.dto.SubCommentDto;
import boaz.site.boazback.study.domain.Study;
import boaz.site.boazback.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class CommentRestControllerTest extends BaseControllerTest {

    private Comment comment1;
    private SubComment subComment1;
    private Page<CommentDto> commentPageable;
    private CommentDto commentDto1;
    private SubCommentDto subCommentDto;
    private Study study;
    private User fakeUser;
    @BeforeEach
    void setUp() {
        baseSetUp();

        study = Study.builder()
                .id(1L)
                .editor(userDto.transForm())
                .contents("test study")
                .hits(0L)
                .build();
        fakeUser = User.builder()
                .email("example@naver.com")
                .password("hello01")
                .name("bob")
                .section(3)
                .year("16")
                .birthDate("0000-00-00")
                .id(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"))
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
        subComment1 = SubComment.builder()
                .id(0L)
                .user(fakeUser)
                .subCommentText("sub test")
                .editName("user2")
                .erase(false)
                .comment(comment1)
                .user(fakeUser)
                .build();
        comment1.getSubComments().add(subComment1);
        commentDto1 = comment1.toDto();
        commentPageable = new PageImpl<CommentDto>(List.of(comment1.toDto()), PageRequest.of(0,1),1);
    }

    @Test
    void getComments() throws Exception {
        given(commentService.getData(anyLong(),any())).willReturn(commentPageable);
        mockMvc.perform(get("/comment/1")
                    .param("page","0")
                    .param("size","5")
                    .cookie(accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.content").isNotEmpty())
                .andExpect(jsonPath("$.payload.pageable").isNotEmpty());
    }
    @Test
    void saveComment() throws Exception{

        given(userService.findUser(any())).willReturn(Optional.of(fakeUser));
        given(commentService.insertData(any(),anyLong(),anyString(),any())).willReturn(commentDto1);
        mockMvc.perform(post("/comment/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"content\":\"test\"}")
                    .cookie(accessToken))
                .andDo(print())
                .andExpect(status().isCreated());
    }
    @Test
    void updateComment() throws Exception{
        given(commentService.updateData(anyLong(),anyString(),any())).willReturn(commentDto1);
        mockMvc.perform(put("/comment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"testUpdate\"}")
                        .cookie(accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload").isNotEmpty());
    }

    @Test
    @DisplayName("댓글 유효성 체크 null")
    void commentValidationNullTest() throws Exception{
        given(commentService.updateData(anyLong(),anyString(),any())).willReturn(commentDto1);
        mockMvc.perform(put("/comment/1")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(accessToken))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("댓글 유효성 체크 empty")
    void commentValidationEmptyTest() throws Exception{
        given(commentService.updateData(anyLong(),anyString(),any())).willReturn(commentDto1);
        mockMvc.perform(post("/comment/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"content\":\"\"}")
                .cookie(accessToken))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }
    @Test
    void deleteComment() throws Exception{
        given(commentService.deleteData(anyLong(),any())).willReturn(0L);
        mockMvc.perform(delete("/comment/1")
                        .cookie(accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload").value(equalTo(0)));
    }
}
