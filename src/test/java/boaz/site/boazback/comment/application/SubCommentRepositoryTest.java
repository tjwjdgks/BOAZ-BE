package boaz.site.boazback.comment.application;

import boaz.site.boazback.BaseDataJpaTest;
import boaz.site.boazback.comment.domain.Comment;
import boaz.site.boazback.comment.domain.SubComment;
import boaz.site.boazback.user.domain.User;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SubCommentRepositoryTest extends BaseDataJpaTest {

    @Test
    void subCommentSave(){
        Optional<User> findUser = userRepository.findById(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"));
        assertTrue(findUser.isPresent());
        Optional<Comment> byId = commentRepository.findById(1L);
        SubComment subComment = SubComment.builder()
                .subCommentText("test")
                .erase(false)
                .user(findUser.get())
                .editName("user1")
                .build();
        subComment.addComment(byId.get());
        SubComment save = subCommentRepository.save(subComment);
        em.flush();
        em.clear();

        assertNotNull(subCommentRepository.findById(save.getId()).get().getId());
    }
    @Test
    void findByComment_Id(){
        List<SubComment> byComment_id = subCommentRepository.findByComment_Id(1L);
        assertEquals(byComment_id.size(),2);
    }
    @Test
    void findBySubComment(){
        Optional<SubComment> byId = subCommentRepository.findById(1L);
        assertTrue(byId.isPresent());
        assertThat(byId.get().getUser().getId().toString()).isEqualTo("df8b21f0-c2d6-11ec-a6d6-0800200c9a66");
        assertThat(byId.get().getComment().getUser().getId().toString()).isEqualTo("df8b21f0-c2d6-11ec-a6d6-0800200c9a67");
    }

}
