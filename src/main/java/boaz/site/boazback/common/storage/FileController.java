package boaz.site.boazback.common.storage;

import boaz.site.boazback.common.domain.CheckJwt;
import boaz.site.boazback.common.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final ObjectStorageUtil objectStorageUtil;

    @PostMapping("/study/{seq}/upload")
    @CheckJwt
    public Result studyUpload(@PathVariable Long seq, MultipartFile file){
        String uploadUrl = objectStorageUtil.upload(file, "study/" + seq);
        Result result = new Result().resultSuccess();
        result.setData(uploadUrl);
        return result;
    }

    @PostMapping("/conference/{seq}/upload")
    @CheckJwt
    public Result conferenceUpload(@PathVariable Long seq, MultipartFile file){
        String uploadUrl = objectStorageUtil.upload(file, "/conference/" + seq);
        Result result = new Result().resultSuccess();
        result.setData(uploadUrl);
        return result;
    }
}
