package boaz.site.boazback.comment.api;


import boaz.site.boazback.comment.application.service.CommentService;
import boaz.site.boazback.comment.domain.ContentWrapper;
import boaz.site.boazback.comment.dto.CommentDto;
import boaz.site.boazback.common.domain.*;
import boaz.site.boazback.common.exception.UserException;
import boaz.site.boazback.user.application.UserService;
import boaz.site.boazback.user.domain.User;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Profile("!prd")
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentRestController {

    private final CommentService commentService;
    private final UserService userService;
    private final Logger logger = LogManager.getLogger();

    @GetMapping("/{studyId}")
    @Logging
    @CheckJwt
    public Result getComments(@PathVariable Long studyId, @PageableDefault(size=5,sort = "createdDate") Pageable pageable){
        Page<CommentDto> data = commentService.getData(studyId, pageable);

        Result result = new Result().resultSuccess();
        result.setData(data);
        return result;
    }
    @PostMapping("/{studyId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Logging
    @CheckJwt
    public Result postComment(@PathVariable Long studyId, @RequestBody @Valid @NotNull ContentWrapper contentWrapper, @JwtLogin JwtInfo jwtInfo){

        logger.info("saveComment start");
        Optional<User> user = userService.findUser(jwtInfo.getId());
        if(!user.isPresent()) throw UserException.USER_NOT_FOUND;
        CommentDto resultDto = commentService.insertData(user.get(),studyId, contentWrapper.getContent(), jwtInfo);
        Result result = new Result().resultSuccess();
        result.setData(resultDto);
        logger.info("saveComment end");
        return result;
    }
    @PutMapping("/{commentId}")
    @CheckJwt
    public Result putComment(@PathVariable Long commentId,@RequestBody @Valid @NotNull ContentWrapper contentWrapper, @JwtLogin JwtInfo jwtInfo){
        logger.info("updateComment start");
        CommentDto commentDto = commentService.updateData(commentId, contentWrapper.getContent(), jwtInfo);
        Result result = new Result().resultSuccess();
        result.setData(commentDto);
        logger.info("updateComment start");
        return result;
    }
    @DeleteMapping("/{commentId}")
    @CheckJwt
    public Result deleteComment(@PathVariable Long commentId, @JwtLogin JwtInfo jwtInfo){
        logger.info("deleteComment start");
        Long deleteCommentId = commentService.deleteData(commentId, jwtInfo);
        Result result = new Result().resultSuccess();
        result.setData(deleteCommentId);
        logger.info("deleteComment start");
        return result;
    }
}
