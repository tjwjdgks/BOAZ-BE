package boaz.site.boazback.intro.application;


import boaz.site.boazback.common.domain.JwtInfo;
import boaz.site.boazback.intro.dto.IntroSubCommentInfo;

import java.util.List;
import java.util.Map;

public interface IntroSubCommentService {
    IntroSubCommentInfo saveSubComment(JwtInfo jwtInfo, String contents, Long id);

    List<IntroSubCommentInfo> getSubCommentsByIntro(Long introId);

    IntroSubCommentInfo updateSubComment(JwtInfo jwtInfo,Long id, String contents);

    IntroSubCommentInfo deleteSubComment(JwtInfo jwtInfo,Long id);
}
