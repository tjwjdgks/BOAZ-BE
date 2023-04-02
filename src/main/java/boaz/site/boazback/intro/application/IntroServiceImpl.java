package boaz.site.boazback.intro.application;


import boaz.site.boazback.common.domain.JwtInfo;
import boaz.site.boazback.intro.domain.Intro;
import boaz.site.boazback.intro.dto.IntroInfo;
import boaz.site.boazback.intro.dto.IntroSubCommentInfo;
import boaz.site.boazback.user.application.UserRepository;
import boaz.site.boazback.user.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class IntroServiceImpl implements IntroService{

    private final UserRepository userRepository;
    private final IntroRepository introRepository;
    private final ObjectMapper objectMapper;

    @Override
    public IntroInfo saveIntro(JwtInfo jwtInfo, String contents) {
        log.info("saveIntro service start");
        Optional<User> userData = userRepository.findById(jwtInfo.getId());
        if (userData.isEmpty()) {
            throw new IllegalStateException("user not found");
        }
            Intro intro = Intro.builder()
                    .contents(contents)
                    .introSubComments(new ArrayList<>())
                    .user(userData.get())
                    .build();
            Intro savedIntro =  introRepository.save(intro);
        log.info("saveIntro service end");
        return new IntroInfo(savedIntro);
    }

    @Override
    public List<IntroInfo> getIntroList(Pageable pageable) {
        log.info("getSubCommentsByIntro service start");
        Page<Intro> introList = introRepository.findByOrderByCreatedDateDesc(pageable);
        List<IntroInfo> introInfoList = introList.getContent().stream().map(intro -> {
            List<IntroSubCommentInfo> subComments = intro.getSubComments().stream().map(IntroSubCommentInfo::new).collect(Collectors.toList());
            IntroInfo info = new IntroInfo(intro);
            info.setSubComments(subComments);
            return info;
        }).collect(Collectors.toList());
        log.info("getSubCommentsByIntro service end");
        return introInfoList;
    }

    @Override
    public IntroInfo updateComment(JwtInfo jwtInfo,Long id, String contents) {
        log.info("updateSubComment service start");
        Intro findIntro = getFindIntro(id,jwtInfo);
        Intro intro = findIntro.updateContent(contents);
        Intro savedIntro= introRepository.save(intro);
        log.info("updateSubComment service end");
        return new IntroInfo(savedIntro);
    }

    @Override
    public IntroInfo deleteComment(JwtInfo jwtInfo,Long id) {
        log.info("delete Comment start ");
        Intro findIntro = getFindIntro(id,jwtInfo);
        Intro newIntro = findIntro.eraseIntro();
        Intro savedIntro = introRepository.save(newIntro);
        log.info("delete Comment end ");
        return new IntroInfo(savedIntro);
    }

    @Override
    public Long getTotalPages(Long size) {
        Long totalSize = introRepository.getCount();
        int pages = totalSize == 0 ? 1 :(int)Math.ceil((double) totalSize / (double) size);
        return Integer.toUnsignedLong(pages);
    }

    private Intro getFindIntro(Long id,JwtInfo jwtInfo) {
        Optional<Intro> findIntro = introRepository.findById(id);
        if (findIntro.isEmpty()) {
            throw new IllegalStateException("intro not exist");
        }
        checkWriteAuthority(findIntro.get().getUser().getId(), jwtInfo);
        return findIntro.get();
    }


    private void checkWriteAuthority(UUID targetId, JwtInfo jwtInfo){
        if (!Objects.equals(targetId, jwtInfo.getId())) {
            throw new IllegalStateException("not same user");
        }
    }
}



