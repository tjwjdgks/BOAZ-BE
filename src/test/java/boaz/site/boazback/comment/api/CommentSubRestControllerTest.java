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

class CommentSubRestControllerTest extends BaseControllerTest {

    Comment comment;
    SubComment subComment;
    CommentDto commentDto;
    SubCommentDto subCommentDto;
    Study study;
    User fakeUser;
    @BeforeEach
    void setUp(){
        baseSetUp();
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
        comment = Comment.builder()
                .id(0L)
                .commentText("test")
                .subComments(new ArrayList<>())
                .editName("user1")
                .erase(false)
                .study(study)
                .user(fakeUser)
                .build();

        subComment = SubComment.builder()
                .subCommentText("subtest")
                .erase(false)
                .editName("user1")
                .comment(comment)
                .user(fakeUser)
                .build();

        commentDto = comment.toDto();
        subCommentDto = subComment.toSubCommentDto();
    }
    @Test
    void getSubComments() throws Exception {
        given(subCommentService.getSubCommentsByCommentId(anyLong())).willReturn(List.of(subCommentDto));
        mockMvc.perform(get("/commentsub/1").cookie(accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload").isNotEmpty());
    }
    @Test
    void postSubComment() throws Exception{
        given(userService.findUser(any())).willReturn(Optional.of(fakeUser));
        given(subCommentService.saveSubComment(any(),anyLong(),anyString(),any())).willReturn(subCommentDto);
        mockMvc.perform(put("/commentsub/1")
                    .content("{\"content\":\"testUpdate\"}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .cookie(accessToken))
                .andDo(print())
                .andExpect(status().isOk());

    }
    @Test
    @DisplayName("대댓글 유효성 체크 content null")
    void postSubCommentValidationNullTest() throws Exception{
        given(userService.findUser(any())).willReturn(Optional.of(fakeUser));
        given(subCommentService.saveSubComment(any(),anyLong(),anyString(),any())).willReturn(subCommentDto);
        mockMvc.perform(post("/commentsub/1")
                .cookie(accessToken))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("대댓글 유효성 체크 content empty")
    void postSubCommentValidationEmptyTest() throws Exception{
        given(userService.findUser(any())).willReturn(Optional.of(fakeUser));
        given(subCommentService.saveSubComment(any(),anyLong(),anyString(),any())).willReturn(subCommentDto);
        mockMvc.perform(post("/commentsub/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"content\":\"\"}")
                .cookie(accessToken))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }
    @Test
    void putSubComment() throws Exception{
        given(subCommentService.updateSubComment(anyLong(),anyString(),any())).willReturn(subCommentDto);
        mockMvc.perform(put("/commentsub/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"content\":\"testUpdate\"}")
                    .cookie(accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload").isNotEmpty());
    }
    @Test
    void deleteSubComment() throws Exception{
        given(subCommentService.deleteSubComment(anyLong(),any())).willReturn(0L);
        mockMvc.perform(delete("/commentsub/1").cookie(accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload").value(equalTo(0)));
    }
}
