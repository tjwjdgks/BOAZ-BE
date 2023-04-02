package boaz.site.boazback.study.application;

import boaz.site.boazback.common.domain.JwtInfo;
import boaz.site.boazback.study.domain.Study;
import boaz.site.boazback.study.dto.StudyDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudyService {
    Study registerStudy(UUID userId);
    Study findStudy(Long studyId);
    List<Study> findStudies(Long studyCategoryId,Pageable pageable);
    Study updateStudy(Long studyId, StudyDto studyDto, JwtInfo jwtInfo);
    void deleteStudy(Long studyId,JwtInfo jwtInfo);
    void undoStudy(Long studyId, JwtInfo jwtInfo,List<String> deleteUrls);
    Long getTotalPages(Long size, Long studyCategoryId);

    String upload(MultipartFile file, String fileUrl);
}
