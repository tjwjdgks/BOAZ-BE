package boaz.site.boazback.study.application;

import boaz.site.boazback.common.domain.JwtInfo;
import boaz.site.boazback.common.storage.ObjectStorageUtil;
import boaz.site.boazback.study.domain.Study;
import boaz.site.boazback.study.domain.StudyCategory;
import boaz.site.boazback.study.dto.StudyDto;
import boaz.site.boazback.user.application.UserRepository;
import boaz.site.boazback.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class StudyServiceImpl implements StudyService {

    private final Logger logger = LogManager.getLogger();

    private final StudyRepository studyRepository;
    private final UserRepository userRepository;
    private final StudyCategoryRepository studyCategoryRepository;
    private final ObjectStorageUtil objectStorageUtil;

    private final String STUDY_FILE_PREFIX = "/study";
    private final Long TOTAL_CATEGORY_ID = 0L;
    @Transactional
    @Override
    public Study registerStudy(UUID userId) {
        logger.info("register service start");
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalStateException("editor not exist"));


        Study study = Study.builder()
                .editor(user)
                .published(false)
                .build();

        Study save = studyRepository.save(study);
        logger.info("register service end");

        return save;
    }


    @Override
    public Study findStudy(Long studyId) {
        logger.info("findStudy service start");
        Optional<Study> findStudy = studyRepository.findById(studyId);
        if (findStudy.isEmpty()) {
            throw new IllegalStateException("study not exist");
        }
        logger.info("findStudy service end");
        return findStudy.get();
    }

    @Override
    public List<Study> findStudies(Long studyCategoryId, Pageable pageable) {
        List<Study> studyList;
        logger.info("findStudies service start");
        Optional<StudyCategory> studyCategory = studyCategoryRepository.findById(studyCategoryId);
        if (studyCategory.isEmpty()) {
            throw new IllegalStateException("study category not exist");
        }
        if (studyCategoryId == TOTAL_CATEGORY_ID) {
            studyList = studyRepository.findByPublishedIsTrueOrderByModifiedDateDesc(pageable);
        } else {
            studyList = studyRepository.findByStudyCategory_IdAndPublishedIsTrueOrderByModifiedDateDesc(studyCategoryId, pageable);
        }
        logger.info("findStudies service end");
        return studyList;
    }

    @Transactional
    @Override
    public Study updateStudy(Long studyId, StudyDto studyDto, JwtInfo jwtInfo) {
        logger.info("updateStudy start");
        Study study = findStudy(studyId);
        StudyCategory studyCategory = study.getStudyCategory();
        checkWriteAuthorityById(jwtInfo.getId(),study.getEditor().getId());

        if(studyCategory ==  null || studyDto.getStudyCategoryId() == null || studyCategory.getId() != studyDto.getStudyCategoryId()){
            updateStudyCategory(study,studyDto.getStudyCategoryId());
        }

        Study savedStudy = study.updateStudy(studyDto.getTitle(),studyDto.getContents(), studyDto.getImgUrl());

        logger.info("after update " +  savedStudy.toString());
        logger.info("updateStudy end");
        return savedStudy;
    }

    private void updateStudyCategory(Study study, Long studyCategoryId) {
        StudyCategory  studyCategory = null;
        if(studyCategoryId == null){
            studyCategory = studyCategoryRepository.findById(TOTAL_CATEGORY_ID)
                .orElseThrow(() -> new IllegalStateException("total category id is invalid"));
        }
        else{
            studyCategory = studyCategoryRepository.findById(studyCategoryId).orElseThrow(()-> new IllegalStateException("category id is invalid"));
        }
        study.updateCategory(studyCategory);
    }

    @Transactional
    @Override
    public void deleteStudy(Long studyId, JwtInfo jwtInfo) {
        logger.info("deleteStudy start");
        Study study = findStudy(studyId);
        checkWriteAuthorityById(jwtInfo.getId(),study.getEditor().getId());
        if(studyId == study.getId()){
            studyRepository.delete(study);
            objectStorageUtil.removeFilesByFilePath(STUDY_FILE_PREFIX+"/" + studyId);
        }
        else throw new IllegalStateException("studyId is not matching");
        logger.info("deleteStudy end");
    }

    @Override
    public void undoStudy(Long studyId, JwtInfo jwtInfo, List<String> deleteUrls) {
        logger.info("undoStudy start");
        Study study = findStudy(studyId);
        checkWriteAuthorityById(jwtInfo.getId(),study.getEditor().getId());
        if(studyId == study.getId()){
            logger.info("remove files start");
            objectStorageUtil.removeFiles(deleteUrls);
            logger.info("remove files end");
        }
        else throw new IllegalStateException("studyId is not matching");
        logger.info("undoStudy end");
    }

    @Override
    public Long getTotalPages(Long size, Long studyCategoryId) {
        Long cnt;
        if (studyCategoryId == TOTAL_CATEGORY_ID){
            cnt = studyRepository.countTotalStudy();
        } else{
            cnt = studyRepository.countTotalStudy(studyCategoryId);
        }
        int pages = cnt == 0 ? 1 : (int) Math.ceil((double) cnt / (double) size);
        return Integer.toUnsignedLong(pages);
    }

    @Override
    public String upload(MultipartFile file, String fileUrl) {
        String uploadUrl = objectStorageUtil.upload(file, fileUrl);
        return uploadUrl;
    }

    private String toDay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void checkWriteAuthorityById(UUID userId, UUID studyOwnerId) {
        if(!userId.equals(studyOwnerId)) throw new IllegalStateException("can't change,user is different");
    }
}
