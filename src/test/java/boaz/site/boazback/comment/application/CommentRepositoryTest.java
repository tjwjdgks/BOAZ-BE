package boaz.site.boazback.comment.application;

import boaz.site.boazback.BaseDataJpaTest;
import boaz.site.boazback.comment.domain.Comment;
import boaz.site.boazback.comment.dto.CommentDto;
import boaz.site.boazback.study.domain.Study;
import boaz.site.boazback.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


class CommentRepositoryTest  extends BaseDataJpaTest {

    @Test
    void commentSave(){
        Optional<User> findUser = userRepository.findById(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"));
        assertTrue(findUser.isPresent());
        Optional<Study> byId = studyRepository.findById(1L);
        Comment comment = Comment.builder()
                .commentText("test2")
                .subComments(new ArrayList<>())
                .editName("user1")
                .user(findUser.get())
                .erase(false)
                .build();
        Comment save = commentRepository.save(comment);
        assertFalse(save.isErase());
        assertEquals(save.getCommentText(),"test2");
    }
    @Test
    void findComment(){
        Optional<Comment> byId = commentRepository.findById(1L);
        assertTrue(byId.isPresent());
        assertThat(byId.get().getUser().getId().toString()).isEqualTo("df8b21f0-c2d6-11ec-a6d6-0800200c9a67");
        assertThat(byId.get().getStudy().getEditor().getId().toString()).isEqualTo("df8b21f0-c2d6-11ec-a6d6-0800200c9a66");
    }
    @Test
    void findCommentsWithStudy(){
        Pageable created_date = PageRequest.of(0, 2, Sort.by(Sort.Order.asc("createdDate")));
        Page<Comment> commentWithStudy = commentRepository.findCommentWithStudy(1L, created_date);
        Page<CommentDto> map = commentWithStudy.map(comment -> comment.toDto());
        assertEquals(map.getTotalElements(),2);
        assertEquals(map.getSize(),2);
        assertEquals(map.getTotalPages(),1);
        assertEquals(map.getContent().size(),2);
        Optional<CommentDto> any = map.getContent().stream().filter(comment -> comment.getCommentId() == 1).findAny();
        assertTrue(any.isPresent());
        assertEquals(any.get().getSubCommentDtoList().size(),2);
    }

    @Test
    void updateData(){
        Optional<Comment> byId = commentRepository.findById(1L);
        assertTrue(byId.isPresent());
        Comment comment = byId.get();
        comment.updateContent("test");
        em.flush();
        em.clear();
        Optional<Comment> byId1 = commentRepository.findById(1L);
        assertEquals(byId1.get().getCommentText(),"test");
    }
    @Test
    void deleteData(){
        Optional<Comment> byId = commentRepository.findById(1L);
        assertTrue(byId.isPresent());
        Comment comment = byId.get();
        comment.eraseContent();
        em.flush();
        em.clear();
        Optional<Comment> byId1 = commentRepository.findById(1L);
        assertTrue(byId1.get().isErase());
    }
}
