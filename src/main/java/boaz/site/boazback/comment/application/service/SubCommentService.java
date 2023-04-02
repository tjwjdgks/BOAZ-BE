package boaz.site.boazback.comment.application.service;

import boaz.site.boazback.comment.dto.SubCommentDto;
import boaz.site.boazback.common.domain.JwtInfo;
import boaz.site.boazback.user.domain.User;

import java.util.List;

public interface SubCommentService {

    List<SubCommentDto> getSubCommentsByCommentId(Long commentId);
    SubCommentDto saveSubComment(User user, Long commentId, String content, JwtInfo jwtInfo);
    SubCommentDto updateSubComment(Long subCommentId,String content, JwtInfo jwtInfo);
    Long deleteSubComment(Long subCommentId,JwtInfo jwtInfo);
}
