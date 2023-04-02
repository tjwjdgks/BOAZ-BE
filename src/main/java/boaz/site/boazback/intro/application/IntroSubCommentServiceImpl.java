package boaz.site.boazback.intro.application;


import boaz.site.boazback.common.domain.JwtInfo;
import boaz.site.boazback.common.exception.AuthorityException;
import boaz.site.boazback.common.exception.UserException;
import boaz.site.boazback.intro.domain.Intro;
import boaz.site.boazback.intro.domain.IntroSubComment;
import boaz.site.boazback.intro.dto.IntroSubCommentInfo;
import boaz.site.boazback.user.application.UserRepository;
import boaz.site.boazback.user.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IntroSubCommentServiceImpl implements IntroSubCommentService {


    private final UserRepository userRepository;
    private final IntroRepository introRepository;
    private final IntroSubCommentRepository introSubCommentRepository;
    private final ObjectMapper objectMapper;
    private final Logger logger = LogManager.getLogger();


    @Override
    public IntroSubCommentInfo saveSubComment(JwtInfo jwtInfo, String contents, Long id) {
        logger.info("saveSubComment service start");
        Optional<User> userData = userRepository.findById(jwtInfo.getId());
        if (userData.isEmpty()) {
            throw UserException.USER_NOT_FOUND;
        }
        Optional<Intro> intro = introRepository.findById(id);
        if (intro.isEmpty()) {
            throw new IllegalStateException("intro not found");
        }
        IntroSubComment introSubComment = IntroSubComment.builder()
                .user(userData.get())
                .intro(intro.get())
                .contents(contents)
                .build();
        IntroSubComment result = introSubCommentRepository.save(introSubComment);
        logger.info("saveSubComment service end");
        return new IntroSubCommentInfo(result);
    }

    @Override
    public List<IntroSubCommentInfo> getSubCommentsByIntro(Long introId) {
        logger.info("getSubCommentsByIntro service start");
        List<IntroSubComment> data = introSubCommentRepository.findByIntroId(introId);
        List<IntroSubCommentInfo> result = data.stream().map(IntroSubCommentInfo::new).collect(Collectors.toList());
        logger.info("getSubCommentsByIntro service end");
        return result;
    }

    @Override
    public IntroSubCommentInfo updateSubComment(JwtInfo jwtInfo,Long id, String contents) {
        logger.info("updateSubComment service start");
        IntroSubComment findIntroSubComment = getFindIntroSubComment(id,jwtInfo);
        IntroSubComment preUpdateSubComment = findIntroSubComment.updateSubComment(contents);
        IntroSubComment updateSubComment = introSubCommentRepository.save(preUpdateSubComment);
        logger.info("updateSubComment service end");
        return new IntroSubCommentInfo(updateSubComment);
    }

    @Override
    public IntroSubCommentInfo deleteSubComment(JwtInfo jwtInfo,Long id) {
        logger.info("deleteSubComment service start");
        IntroSubComment findIntroSubComment = getFindIntroSubComment(id,jwtInfo);
        IntroSubComment preDeleteIntroSubComment = findIntroSubComment.eraseSubComment();
        IntroSubComment savedSubComment = introSubCommentRepository.save(preDeleteIntroSubComment);
        logger.info("deleteSubComment service end");
        return new IntroSubCommentInfo(savedSubComment);
    }


    private IntroSubComment getFindIntroSubComment(Long id,JwtInfo jwtInfo) {
        Optional<IntroSubComment> findIntroSubComment = introSubCommentRepository.findById(id);
        if (findIntroSubComment.isEmpty()) {
            throw new IllegalStateException("intro subComment not found");
        }
        checkWriteAuthority(findIntroSubComment.get().getUser().getId(),jwtInfo);
        return findIntroSubComment.get();
    }

    private void checkWriteAuthority(UUID targetId, JwtInfo jwtInfo){
        if (!Objects.equals(targetId, jwtInfo.getId())) {
            throw AuthorityException.EDITOR_ERROR;
        }
    }


}
