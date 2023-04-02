package boaz.site.boazback.comment.application;


import boaz.site.boazback.comment.application.repository.CommentRepository;
import boaz.site.boazback.comment.application.service.CommentService;
import boaz.site.boazback.comment.domain.Comment;
import boaz.site.boazback.comment.dto.CommentDto;
import boaz.site.boazback.common.domain.JwtInfo;
import boaz.site.boazback.common.domain.Result;
import boaz.site.boazback.study.application.StudyRepository;
import boaz.site.boazback.study.domain.Study;
import boaz.site.boazback.user.application.UserRepository;
import boaz.site.boazback.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final StudyRepository studyRepository;
    @Transactional
    @Override
    public CommentDto insertData(User user, Long studyId, String content, JwtInfo jwtInfo) {
        log.info("commentService insertData start");
        Optional<Study> study = studyRepository.findById(studyId);
        if(study.isEmpty()) throw new IllegalStateException("not exist study");
        Comment comment = Comment.builder()
                .user(user)
                .commentText(content)
                .subComments(new ArrayList<>())
                .editName(jwtInfo.getEditName())
                .erase(false)
                .study(study.get())
                .build();
        Comment save = commentRepository.save(comment);
        log.info("commentService insertData end");
        return save.toDto();
    }

    @Override
    public Page<CommentDto> getData(Long studyId, Pageable pageable) {
        log.info("commentService getData start");
        Page<Comment> commentWithStudy = commentRepository.findCommentWithStudy(studyId, pageable);
        Page<CommentDto> commentDto = commentWithStudy.map(comment -> comment.toDto());
        log.info("commentService getData end");
        return commentDto;
    }

    @Transactional
    @Override
    public CommentDto updateData(Long commentId,String content, JwtInfo jwtInfo) {
        log.info("commentService updateData start");
        Comment comment = findComment(commentId, jwtInfo);
        Comment updateContent = comment.updateContent(content);
        log.info("commentService updateData end");
        return updateContent.toDto();
    }

    @Transactional
    @Override
    public Long deleteData(Long commentId, JwtInfo jwtInfo) {
        log.info("commentService deleteData start");
        Comment comment = findComment(commentId, jwtInfo);
        comment.eraseContent();
        log.info("commentService deleteData end");
        return commentId;
    }

    private Comment findComment(Long commentId,JwtInfo jwtInfo) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if(comment.isEmpty())
            throw new IllegalStateException("comment not exist");
        checkWriteAuthority(comment.get().getUser().getId(),jwtInfo.getId());
        return comment.get();
    }
    private void checkWriteAuthority(UUID commentEditor, UUID userName) {
        if(!commentEditor.equals(userName)) throw new IllegalStateException("can't change,user is different");
    }
}
