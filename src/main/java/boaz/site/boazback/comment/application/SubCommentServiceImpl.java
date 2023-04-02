package boaz.site.boazback.comment.application;

import boaz.site.boazback.comment.application.repository.CommentRepository;
import boaz.site.boazback.comment.application.repository.SubCommentRepository;
import boaz.site.boazback.comment.application.service.SubCommentService;

import boaz.site.boazback.comment.domain.Comment;
import boaz.site.boazback.comment.domain.SubComment;
import boaz.site.boazback.comment.dto.SubCommentDto;
import boaz.site.boazback.common.domain.JwtInfo;
import boaz.site.boazback.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SubCommentServiceImpl implements SubCommentService {

    private final SubCommentRepository subCommentRepository;
    private final CommentRepository commentRepository;
    @Override
    public List<SubCommentDto> getSubCommentsByCommentId(Long commentId) {
        log.info("getSubCommentsByCommentId service start");
        List<SubComment> subComments = subCommentRepository.findByComment_Id(commentId);
        List<SubCommentDto> result = subComments.stream().map(SubComment::toSubCommentDto).collect(Collectors.toList());
        log.info("getSubCommentsByCommentId service end");
        return result;
    }

    @Transactional
    @Override
    public SubCommentDto saveSubComment(User user, Long commentId, String content, JwtInfo jwtInfo) {
        log.info("saveSubComment service start");
        Optional<Comment> comment = commentRepository.findById(commentId);
        if(comment.isEmpty()){
            throw new IllegalStateException("comment not found");
        }
        SubComment subComment = SubComment.builder()
                .subCommentText(content)
                .erase(false)
                .user(user)
                .editName(jwtInfo.getEditName())
                .build();

        subComment.addComment(comment.get());
        subCommentRepository.save(subComment);
        log.info("saveSubComment service end");
        return subComment.toSubCommentDto();
    }
    @Transactional
    @Override
    public SubCommentDto updateSubComment(Long subCommentId, String content, JwtInfo jwtInfo) {
        log.info("updateSubComment service start");
        SubComment subComment = findSubComment(subCommentId,jwtInfo);
        SubComment updateContent = subComment.updateContent(content);
        log.info("updateSubComment service end");
        return updateContent.toSubCommentDto();
    }

    @Transactional
    @Override
    public Long deleteSubComment(Long subCommentId, JwtInfo jwtInfo) {
        log.info("deleteSubComment service start");
        SubComment subComment = findSubComment(subCommentId, jwtInfo);
        subComment.eraseContent();
        log.info("deleteSubComment service end");
        return subCommentId;
    }

    private SubComment findSubComment(Long subCommentId, JwtInfo jwtInfo) {
        Optional<SubComment> subComment = subCommentRepository.findById(subCommentId);
        if(subComment.isEmpty()){
            throw new IllegalStateException("subComment not found");
        }
        checkWriteAuthority(subComment.get().getUser().getId(),jwtInfo.getId());
        return subComment.get();
    }

    private void checkWriteAuthority(UUID subCommentEditor, UUID userName) {
        if(!subCommentEditor.equals(userName)) throw new IllegalStateException("can't change,user is different");
    }
}
