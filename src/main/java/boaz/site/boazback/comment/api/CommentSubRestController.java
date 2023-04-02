package boaz.site.boazback.comment.api;


import boaz.site.boazback.comment.application.service.SubCommentService;
import boaz.site.boazback.comment.domain.ContentWrapper;
import boaz.site.boazback.comment.dto.SubCommentDto;
import boaz.site.boazback.common.domain.*;
import boaz.site.boazback.common.exception.UserException;
import boaz.site.boazback.user.application.UserService;
import boaz.site.boazback.user.domain.User;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.units.qual.C;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Profile("!prd")
@RestController
@RequiredArgsConstructor
@RequestMapping("/commentsub")
public class CommentSubRestController {

    private final SubCommentService subCommentService;
    private final UserService userService;
    @GetMapping("/{commentId}")
    @Logging
    public Result getSubComments(@PathVariable Long commentId){
        log.info("getSubComments start");
        List<SubCommentDto> subCommentsByCommentId = subCommentService.getSubCommentsByCommentId(commentId);
        Result result = new Result().resultSuccess();
        result.setData(subCommentsByCommentId);
        log.info("getSubComments end");
        return  result;
    }
    @PostMapping("/{commentId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Logging
    @CheckJwt
    public Result postSubComment(@PathVariable Long commentId, @RequestBody @Valid @NotNull @NotEmpty ContentWrapper contentWrapper, @JwtLogin JwtInfo jwtInfo){
        log.info("postSubComment start");
        Optional<User> user = userService.findUser(jwtInfo.getId());
        if(!user.isPresent()) throw UserException.USER_NOT_FOUND;
        SubCommentDto data = subCommentService.saveSubComment(user.get(),commentId, contentWrapper.getContent(), jwtInfo);
        Result result = new Result().resultSuccess();
        result.setData(data);
        log.info("postSubComment end");
        return result;
    }

    @PutMapping("/{commentSubId}")
    @Logging
    @CheckJwt
    public Result putSubComment(@PathVariable Long commentSubId, @RequestBody @Valid @NotNull @NotEmpty ContentWrapper contentWrapper, @JwtLogin JwtInfo jwtInfo){
        log.info("putSubComment start");
        subCommentService.updateSubComment(commentSubId, contentWrapper.getContent(), jwtInfo);
        Result result = new Result().resultSuccess();
        log.info("putSubComment end");

        return result;
    }
    @DeleteMapping("/{commentSubId}")
    @Logging
    @CheckJwt
    public Result deleteSubComment(@PathVariable Long commentSubId, @JwtLogin JwtInfo jwtInfo){
        log.info("deleteSubComment start");
        Long deleteSubCommentId = subCommentService.deleteSubComment(commentSubId, jwtInfo);
        Result result = new Result().resultSuccess();
        result.setData(deleteSubCommentId);
        log.info("deleteSubComment end");
        return result;
    }
}
