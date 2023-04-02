package boaz.site.boazback.intro.controller;

import boaz.site.boazback.common.domain.*;
import boaz.site.boazback.intro.application.IntroSubCommentService;
import boaz.site.boazback.intro.dto.IntroContentRegister;
import boaz.site.boazback.intro.dto.IntroSubCommentInfo;
import boaz.site.boazback.intro.dto.IntroSubContentRegister;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
@Profile("!prd")
@RestController
@RequiredArgsConstructor
@RequestMapping("/introsub")
@Slf4j
public class IntroSubRestController {

    private final IntroSubCommentService introSubCommentService;

    @GetMapping("/{id}")
    @Transactional
    @Logging
    @CheckJwt
    public Result getIntroSubComments(@PathVariable Long id) {
        log.info("getIntroSubComments start");
        Result result = new Result().resultSuccess();
        List<IntroSubCommentInfo> data = introSubCommentService.getSubCommentsByIntro(id);
        result.setData(data);
        log.info("getIntroSubComments end");
        return result;
    }

    @PostMapping
    @CheckJwt
    @Logging
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public Result postIntroSubComment(@RequestBody @Valid IntroSubContentRegister introContentRegister, @JwtLogin JwtInfo jwtInfo) {
        log.info("postIntroSubComment start");
        Result result = new Result().resultSuccess();
        IntroSubCommentInfo data = introSubCommentService.saveSubComment(jwtInfo, introContentRegister.getContent(), introContentRegister.getCommentId());
        result.setData(data);
        log.info("postIntroSubComment end");
        return result;
    }

    @PutMapping("/{id}")
    @CheckJwt
    @Logging
    @Transactional
    public Result putIntroSubComment(@PathVariable Long id, @RequestBody @Valid IntroContentRegister introContentRegister, @JwtLogin JwtInfo jwtInfo) {
        log.info("putIntroSubComment start");
        Result result = new Result().resultSuccess();
        IntroSubCommentInfo introSubCommentInfo = introSubCommentService.updateSubComment(jwtInfo, id, introContentRegister.getContent());
        result.setData(introSubCommentInfo);
        log.info("putIntroSubComment end");
        return result;
    }

    @DeleteMapping("/{id}")
    @CheckJwt
    @Logging
    @Transactional
    public Result deleteIntroSubComment(@PathVariable Long id, @JwtLogin JwtInfo jwtInfo) {
        log.info("deleteIntroSubComment start");
        Result result = new Result().resultSuccess();
        IntroSubCommentInfo introSubCommentInfo = introSubCommentService.deleteSubComment(jwtInfo, id);
        result.setData(introSubCommentInfo);
        log.info("deleteIntroSubComment end");
        return result;
    }
}
