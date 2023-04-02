package boaz.site.boazback.question.api;

import boaz.site.boazback.common.domain.Result;
import boaz.site.boazback.question.application.QuestionService;
import boaz.site.boazback.question.domain.Question;
import boaz.site.boazback.question.dto.QuestionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("question")
@RequiredArgsConstructor
@Slf4j
public class QuestionController {


    private final QuestionService questionService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Result sendQuestion(@RequestBody @Valid QuestionDto questionDto){
        log.info("sendQuestion start");
        Result result = new Result().resultSuccess();
        Question sentQuestion = questionService.sendInfo(questionDto);
        result.setData(sentQuestion);
        log.info("sendQuestion end");
        return result;

    }

    @GetMapping("/{uid}")
    public Result findQuestion(@PathVariable("uid") Long uid) {
        log.info("findQuestion start");
        Result result = new Result().resultSuccess();
        Question findQuestion = questionService.findQuestion(uid);
        result.setData(findQuestion);
        log.info("findQuestion end");
        return result;
    }

}
