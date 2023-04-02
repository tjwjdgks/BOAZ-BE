package boaz.site.boazback.intro.controller;

import boaz.site.boazback.common.domain.*;
import boaz.site.boazback.intro.application.IntroService;
import boaz.site.boazback.intro.dto.IntroContentRegister;
import boaz.site.boazback.intro.dto.IntroInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Profile("!prd")
@RequestMapping("/intro")
@RestController
@RequiredArgsConstructor
@Slf4j
public class IntroRestController {

    private final IntroService introService;

    @GetMapping
    @Transactional
    @CheckJwt
    public Result getMemberIntro(Pageable pageable, @JwtLogin JwtInfo jwtInfo){
        Result result = new Result().resultSuccess();
        log.info("getMemberIntro start");
        List<IntroInfo> introInfoList = introService.getIntroList(pageable);
        result.setData(introInfoList);
        log.info("getMemberIntro end");
        return result;
    }

    @GetMapping("/page")
    @CheckJwt
    public Result getIntroTotalPages(@RequestParam("size") Long size) {
        log.info("get intro total pages start");
        Result result = new Result().resultSuccess();
        Long totalPages = introService.getTotalPages(size);
        result.setData(totalPages);
        log.info("get intro total pages end");
        return result;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CheckJwt
    @Logging
    @Transactional
    public Result postMemberIntro(@RequestBody @Valid  @NotNull @NotEmpty IntroContentRegister introContentRegister, @JwtLogin JwtInfo jwtInfo) {
        // 쿠키로 user info 추출
        log.info("postMemberIntro start");
        Result result = new Result().resultSuccess();
        //데이터 저장하기
        IntroInfo intro = introService.saveIntro(jwtInfo, introContentRegister.getContent());
        result.setData(intro);
        log.info("postMemberIntro end");
        return result;
    }

    @PutMapping("/{id}")
    @CheckJwt
    @Logging
    @Transactional
    public Result putMemberIntro(@PathVariable Long id, @RequestBody @Valid  @NotNull @NotEmpty IntroContentRegister introContentRegister, @JwtLogin JwtInfo jwtInfo){
        // 쿠키로 user info 추출
        log.info("putMemberIntro start");
        Result result  = new Result().resultSuccess();
        IntroInfo success =  introService.updateComment(jwtInfo,id, introContentRegister.getContent());
        result.setData(success);
        log.info("putMemberIntro end");
        // 객체를 만들어서 post
        return result;
    }

    @DeleteMapping("/{id}")
    @CheckJwt
    @Logging
    @Transactional
    public Result deleteMemberIntro(@PathVariable  Long id,@JwtLogin JwtInfo jwtInfo){
        log.info("deleteMemberIntro start");
        introService.deleteComment(jwtInfo,id);
        log.info("deleteMemberIntro end");
        return new Result().resultSuccess();
    }

}
