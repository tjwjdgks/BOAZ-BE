package boaz.site.boazback.study.api;

import boaz.site.boazback.common.domain.CheckJwt;
import boaz.site.boazback.common.domain.JwtInfo;
import boaz.site.boazback.common.domain.JwtLogin;
import boaz.site.boazback.common.domain.Result;
import boaz.site.boazback.common.exception.ImageException;
import boaz.site.boazback.study.application.StudyService;
import boaz.site.boazback.study.domain.Study;
import boaz.site.boazback.common.domain.DeleteFileUrlWrapper;
import boaz.site.boazback.study.dto.StudyDto;
import boaz.site.boazback.user.application.UserService;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
@Profile("!prd")
@RestController
@RequestMapping("/study")
@RequiredArgsConstructor
public class StudyController {

    private final Logger logger = LogManager.getLogger();
    private final StudyService studyService;
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CheckJwt
    public Result registerStudy(@JwtLogin JwtInfo jwtInfo){
        logger.info("init study start");
        if(!userService.isExistUser(jwtInfo.getId())) throw new IllegalStateException("user not found");
        Result result = new Result().resultSuccess();
        Study study = studyService.registerStudy(jwtInfo.getId());
        result.setData(study);
        logger.info("init study end");
        return result;
    }
    // check
    @GetMapping("/detail/{studyId}")
    @CheckJwt
    public Result findStudy(@PathVariable("studyId") Long studyId){
        Result result = new Result().resultSuccess();
        logger.info("find study start");
        Study study = studyService.findStudy(studyId);
        result.setData(study);
        logger.info("find study end");
        return result;
    }

    @GetMapping("/{studyCategoryId}")
    @CheckJwt
    public Result findStudyInventory(@PathVariable("studyCategoryId") Long studyCategoryId, Pageable pageable) {
        Result result = new Result().resultSuccess();
        logger.info("find study inventory start");
        List<Study> studyList = studyService.findStudies(studyCategoryId, pageable);
        result.setData(studyList);
        logger.info("find study inventory end");
        return result;
    }


    //check
    @GetMapping("/page")
    @CheckJwt
    public Result getStudyTotalPages(@RequestParam("size") Long size ,@RequestParam("studyCategoryId")Long studyCategoryId) {
        logger.info("get study total pages start");
        Result result = new Result().resultSuccess();
        Long totalPages = studyService.getTotalPages(size,studyCategoryId);
        result.setData(totalPages);
        logger.info("get study total pages end");
        return result;
    }

    // check
    @DeleteMapping("/{studyId}")
    @CheckJwt
    public Result deleteStudy(@PathVariable("studyId") Long studyId, @JwtLogin JwtInfo jwtInfo) {
        Result result = new Result().resultSuccess();
        logger.info("delete study start");
        studyService.deleteStudy(studyId, jwtInfo);

        logger.info("delete study end");
        return result;
    }
    //
    @PostMapping("/{studyId}/undo")
    @CheckJwt
    public Result undoStudy(@PathVariable("studyId") Long studyId, @RequestBody DeleteFileUrlWrapper deleteFileUrlWrapper, @JwtLogin JwtInfo jwtInfo){
        Result result = new Result().resultSuccess();
        logger.info("undo study start");
        studyService.undoStudy(studyId,jwtInfo, deleteFileUrlWrapper.getFileUrls());
        logger.info("undo study end");
        return result;
    }

    @PutMapping("/{studyId}")
    @CheckJwt
    public Result updateStudy(@PathVariable("studyId") Long studyId,
                                @Valid @NotNull StudyDto studyDto, @JwtLogin JwtInfo jwtInfo){
        logger.info("update study start");
        Result result = new Result().resultSuccess();
        Study study = studyService.updateStudy(studyId, studyDto,jwtInfo);
        result.setData(study);
        logger.info("update study end");
        return result;
    }

    @PostMapping("/{studyId}/file")
    @ResponseStatus(HttpStatus.CREATED)
    @CheckJwt
    @Validated
    public Result studyUpload(@PathVariable Long studyId, MultipartFile file){
        if (file == null) throw ImageException.FILE_NULL;
        String uploadUrl = studyService.upload(file,"study/" + studyId);
        Result result = new Result().resultSuccess();
        result.setData(uploadUrl);
        return result;
    }
}
