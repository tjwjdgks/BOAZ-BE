package boaz.site.boazback.intro.application;


import boaz.site.boazback.common.domain.JwtInfo;
import boaz.site.boazback.intro.dto.IntroInfo;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IntroService {

    IntroInfo saveIntro(JwtInfo jwtInfo, String contents);

    List<IntroInfo> getIntroList(Pageable pageable);

    IntroInfo updateComment(JwtInfo jwtInfo,Long id, String contents);

    IntroInfo deleteComment(JwtInfo jwtInfo,Long id);
    Long getTotalPages(Long size);
}
