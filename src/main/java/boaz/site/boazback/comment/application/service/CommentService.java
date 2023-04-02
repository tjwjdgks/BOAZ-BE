package boaz.site.boazback.comment.application.service;


import boaz.site.boazback.comment.dto.CommentDto;
import boaz.site.boazback.common.domain.JwtInfo;
import boaz.site.boazback.common.domain.Result;
import boaz.site.boazback.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
    CommentDto insertData(User user, Long studyId, String content, JwtInfo jwtInfo);
    Page<CommentDto> getData(Long studyId, Pageable pageable);
    CommentDto updateData(Long commentId,String content, JwtInfo jwtInfo);
    Long deleteData(Long id, JwtInfo jwtInfo);
}
