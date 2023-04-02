package boaz.site.boazback.conference.api;

import boaz.site.boazback.common.domain.*;
import boaz.site.boazback.conference.application.ConferenceService;
import boaz.site.boazback.conference.domain.Conference;
import boaz.site.boazback.conference.dto.ConferenceDto;
import boaz.site.boazback.conference.dto.ConferenceInfo;
import boaz.site.boazback.conference.dto.ConferenceUpdateInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Profile("!prd")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/conference")
public class ConferenceController {

    private final ConferenceService conferenceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CheckJwt
    public Result registerConference(@JwtLogin JwtInfo jwtInfo){
        Result result = new Result().resultSuccess();
        log.info("Registering Conference start");
        ConferenceDto.InitConferenceDto initdConference = conferenceService.registerConference(jwtInfo);
        result.setData(initdConference);
        log.info("Registering Conference start");
        return result;
    }

    @GetMapping("/{uid}")
    public Result findConference(@PathVariable("uid") Long uid) {
        Result result = new Result().resultSuccess();
        log.info("Find Conference start");
        Conference conference = conferenceService.findConference(uid);
        result.setData(conference);
        log.info("Find Conference end");
        return result;
    }

    @GetMapping
    public Result findConferencePagination(Pageable pageable) {
        Result result = new Result().resultSuccess();
        log.info("Find Conference pagination start");
        Page<ConferenceInfo> conferenceList = conferenceService.findConferencePagination(pageable);
        result.setData(conferenceList);
        log.info("Find Conference pagination end");
        return result;
    }

    @DeleteMapping("/{uid}")
    @CheckJwt
    public Result deleteConference(@PathVariable Long uid, @JwtLogin JwtInfo jwtInfo) {
        Result result = new Result().resultSuccess();
        log.info("Deleting Conference start");
        conferenceService.deleteConference(uid,jwtInfo);
        log.info("Deleting Conference end");
        return result;
    }

    @PutMapping("/{uid}")
    @CheckJwt
    public Result updateConference(@PathVariable Long uid, @JwtLogin JwtInfo jwtInfo, @RequestBody @Valid ConferenceUpdateInfo conferenceUpdateInfo) {
        Result result = new Result().resultSuccess();
        log.info("Update Conference  start");
        boolean res = conferenceService.updateConference(uid,  conferenceUpdateInfo, jwtInfo);
        System.out.println("res = " + res);
        log.info("UpdateConference  end");
        return result;
    }

    @GetMapping("/search")
    public Result searchConferenceList(@RequestParam("query")String title,@PageableDefault(size = 5) Pageable pageable){
        log.info("searchConferenceList start");
        Result result = new Result().resultSuccess();
        Page<ConferenceInfo> pagedConferenceInfo = conferenceService.searchConferenceListForTitle(title, pageable);
        result.setData(pagedConferenceInfo);
        log.info("searchConferenceList end");
        return result;
    }


    @PostMapping("/{conference-id}/image")
    @ResponseStatus(HttpStatus.CREATED)
    @CheckJwt
    public Result uploadConferenceImage(@PathVariable("conference-id") Long conferenceId,@JwtLogin JwtInfo jwtInfo ,MultipartFile file){
        log.info("uploadConferenceImage start");
        Result result = new Result().resultSuccess();
        String uploadUrl = conferenceService.uploadImage(conferenceId,jwtInfo, file);
        result.setData(uploadUrl);
        log.info("uploadConferenceImage end");
        return result;
    }

    @PostMapping("/{conference-id}/delete-image")
    @CheckJwt
    public Result deleteConferenceImage(@PathVariable("conference-id") Long conferenceId, @JwtLogin JwtInfo jwtInfo , @RequestBody DeleteFileUrlWrapper deleteFileUrlWrapper){
        log.info("uploadConferenceImage start");
        Result result = new Result().resultSuccess();
        conferenceService.deleteConferenceImage(conferenceId, jwtInfo, deleteFileUrlWrapper.getFileUrl());
        log.info("uploadConferenceImage end");
        return result;
    }

    @PostMapping("/{conference-id}/undo")
    @CheckJwt
    public Result undoConference(@PathVariable("conference-id") Long conferenceId,@JwtLogin JwtInfo jwtInfo,@RequestBody DeleteFileUrlWrapper deleteFileUrlWrapper){
        log.info("uploadConferenceImage start");
        Result result = new Result().resultSuccess();
        conferenceService.undoConference(conferenceId, jwtInfo, deleteFileUrlWrapper.getFileUrls());
        log.info("uploadConferenceImage end");
        return result;
    }

    @PostMapping("/{conference-id}/file")
    @ResponseStatus(HttpStatus.CREATED)
    @CheckJwt
    public Result uploadConferenceFile(@PathVariable("conference-id") Long conferenceId,@JwtLogin JwtInfo jwtInfo ,MultipartFile file){
        log.info("uploadConferenceImage start");
        Result result = new Result().resultSuccess();
        String uploadUrl = conferenceService.uploadFile(conferenceId,jwtInfo, file);
        result.setData(uploadUrl);
        log.info("uploadConferenceImage end");
        return result;
    }

    @PostMapping("/{conference-id}/delete-file")
    @CheckJwt
    public Result deleteConferenceFile(@PathVariable("conference-id") Long conferenceId, @JwtLogin JwtInfo jwtInfo , @RequestBody DeleteFileUrlWrapper deleteFileUrlWrapper){
        log.info("uploadConferenceImage start");
        Result result = new Result().resultSuccess();
        conferenceService.deleteConferenceFile(conferenceId, jwtInfo, deleteFileUrlWrapper.getFileUrl());
        log.info("uploadConferenceImage end");
        return result;
    }

}
