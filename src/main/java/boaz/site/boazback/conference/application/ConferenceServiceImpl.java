package boaz.site.boazback.conference.application;

import boaz.site.boazback.common.domain.JwtInfo;
import boaz.site.boazback.common.domain.TracingFunction;
import boaz.site.boazback.common.exception.AuthorityException;
import boaz.site.boazback.common.exception.ConferenceException;
import boaz.site.boazback.common.exception.UserException;
import boaz.site.boazback.common.storage.ObjectStorageUtil;
import boaz.site.boazback.conference.domain.Conference;
import boaz.site.boazback.conference.dto.ConferenceDto;
import boaz.site.boazback.conference.dto.ConferenceInfo;
import boaz.site.boazback.conference.dto.ConferenceUpdateInfo;
import boaz.site.boazback.user.application.UserRepository;
import boaz.site.boazback.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.text.SimpleDateFormat;
import java.util.*;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConferenceServiceImpl implements ConferenceService {

    private final ConferenceRepository conferenceRepository;
    private final UserRepository userRepository;
    private final ObjectStorageUtil objectStorageUtil;

    private final String CONFERENCE_FILE_PREFIX = "conference/file/";
    private final String CONFERENCE_IMAGE_PREFIX = "conference/image/";

    private final EntityManager entityManager;

    private final ConferenceQueryRepositoryImpl conferenceQueryRepository;

    @Override
    @Transactional
    public ConferenceDto.InitConferenceDto registerConference(JwtInfo jwtInfo) {
        log.info("init conference service start");
        User editor = userRepository.findById(jwtInfo.getId())
                .orElseThrow(() -> {
                    throw UserException.USER_NOT_FOUND;
                });
        Conference newConference = Conference.initBuilder()
                .published(false)
                .editor(editor)
                .init();
        Conference savedConference = conferenceRepository.save(newConference);
        log.info("init conference service end");
        return new ConferenceDto.InitConferenceDto(savedConference);
    }

    @Transactional
    @Override
    public void deleteConference(Long uid, JwtInfo jwtInfo) {
        log.info("deleteConference service start");
        getConference(uid, jwtInfo);
        conferenceRepository.deleteById(uid);
        objectStorageUtil.removeFilesByFilePath(CONFERENCE_FILE_PREFIX + uid);
        log.info("deleteConference service end");
    }

    @Override
    @Transactional(readOnly = true)
    public Conference findConference(Long uid) {
        log.info("findConference service start");
        Conference conference = conferenceRepository.findByIdAndPublished(uid, true)
                .orElseThrow(() -> {
                    throw ConferenceException.CONFERENCE_NOT_FOUND;
                });
        log.info("findConference service end");
        return conference;
    }

    @Override
    @Transactional(readOnly = true)
    public int countAllConference() {
        log.info("countAllConference start");
        List<Conference> conferenceList = conferenceRepository.findByPublished(true);
        log.info("countAllConference end");
        return conferenceList.size();
    }

    @Override
    @Transactional
    public boolean updateConference(Long uid, ConferenceUpdateInfo conferenceUpdateInfo, JwtInfo jwtInfo) {
        log.info("updateConference start");
        Conference findConference = conferenceRepository.findById(uid)
                .orElseThrow(() -> {
                    throw ConferenceException.CONFERENCE_NOT_FOUND;
                });
        checkWriteAuthority(findConference.getEditor().getId(), jwtInfo);
        Conference conference = findConference.publishConference(conferenceUpdateInfo);
        conferenceRepository.save(conference);
        log.info("updateConference end");
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    @TracingFunction
    public Page<ConferenceInfo> findConferencePagination(Pageable pageable) {
        log.info("findConference pagination start");
        Page<Conference> result = conferenceRepository.findByPublishedOrderByCreatedDateDesc(true, pageable);
        List<ConferenceInfo> result2 = result.getContent().stream().map(ConferenceInfo::new).collect(Collectors.toList());
        Page<ConferenceInfo> data = new PageImpl<>(result2, pageable, result.getTotalElements());
        log.info("findConference pagination end");
        return data;
    }

    /**
     * 컨퍼런스 제목 기반의 컨퍼런스 검색
     *
     * @param title    컨퍼런스 제목
     * @param pageable 페이징처리 요소
     * @return title에 특정 요청 단어가 포함된 ConferenceInfo 리스트들을 Paginagtion하여 반환해줍니다.
//     * @see ConferenceRepository#findByTitleContainsOrderByCreatedDateDesc(String, Pageable)
     @see ConferenceQueryRepository#findConferenceByTitleAndWriterOrderByCreatedDateDesc(Pageable, String)
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ConferenceInfo> searchConferenceListForTitle(String title, Pageable pageable) {
        log.info("searchConferenceListForTitle start");
//        Page<ConferenceInfo> conferenceInfoPage = conferenceRepository.findByTitleContainsAndPublishedOrderByCreatedDateDesc(title, true, pageable);
        Page<Conference> conferencePage = conferenceQueryRepository.findConferenceByTitleAndWriterOrderByCreatedDateDesc(pageable, title);
        List<ConferenceInfo> data = conferencePage.getContent()
                        .stream()
                        .map(ConferenceInfo::new)
                        .collect(Collectors.toList());
        log.info("searchConferenceListForTitle end");
        return new PageImpl<ConferenceInfo>(data,pageable,conferencePage.getTotalElements());
    }


    /**
     * cloud object storage에  image 업도르하는 서비스
     *
     * @param conferenceId 컨퍼런스 id
     * @param jwtInfo      jwt토큰 정보
     * @param imgFile      image file
     * @return cloud object storage에 올라간 image 경로를 반환
     * @see ObjectStorageUtil#upload(MultipartFile, String)
     */
    @Override
    public String uploadImage(Long conferenceId, JwtInfo jwtInfo, MultipartFile imgFile) {
        log.info("uploadImage Service Start");
        log.info(">> {} ", conferenceId);
        getConference(conferenceId, jwtInfo);
        String uploadUrl = objectStorageUtil.upload(imgFile, CONFERENCE_IMAGE_PREFIX + conferenceId);
        log.info("uploadImage Service End");
        return uploadUrl;
    }

    /**
     * cloud object storage에 저장되어 있는 image 삭제하는 서비스
     *
     * @param conferenceId 컨퍼런스 id
     * @param jwtInfo      jwt토큰 정보
     * @param imageUrl     삭제할 image주소
     * @see ObjectStorageUtil#removeFile(String)
     */
    @Override
    public void deleteConferenceImage(Long conferenceId, JwtInfo jwtInfo, String imageUrl) {
        log.info("deleteConferenceImage service start");
        getConference(conferenceId, jwtInfo);
        objectStorageUtil.removeFile(imageUrl);
        log.info("deleteConferenceImage service end");
    }

    /**
     * 임시저장상태일 컨퍼런스 삭제하기  + s3에 저장되어 있는 이미지 삭제
     *
     * @param conferenceId 컨퍼런스id
     * @param jwtInfo      jwt토큰 정보
     * @param imageUrlList 저장된 imageUrllist
     * @see ObjectStorageUtil#removeFiles(List)
     * @see ConferenceRepository#deleteById(Object)
     */
    @Override
    public void undoConference(Long conferenceId, JwtInfo jwtInfo, List<String> imageUrlList) {
        log.info("undoConference service start");
        getConference(conferenceId, jwtInfo);
        objectStorageUtil.removeFiles(imageUrlList);
        conferenceRepository.deleteById(conferenceId);
        log.info("undoConference service end");
    }

    @Override
    public String uploadFile(Long conferenceId, JwtInfo jwtInfo, MultipartFile file) {
        log.info("uploadImage Service Start");
        log.info(">> {} ", conferenceId);
        getConference(conferenceId, jwtInfo);
        String uploadUrl = objectStorageUtil.upload(file, CONFERENCE_FILE_PREFIX + conferenceId);
        log.info("uploadImage Service End");
        return uploadUrl;
    }

    @Override
    public void deleteConferenceFile(Long conferenceId, JwtInfo jwtInfo, String fileUrl) {
        log.info("deleteConferenceImage service start");
        getConference(conferenceId, jwtInfo);
        objectStorageUtil.removeFile(fileUrl);
        log.info("deleteConferenceImage service end");
    }

    private String toDay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        return dateFormat.format(date);
    }


    private void getConference(Long id, JwtInfo jwtInfo) {
        Optional<Conference> findConference = conferenceRepository.findById(id);
        if (findConference.isEmpty()) {
            throw ConferenceException.CONFERENCE_NOT_FOUND;
        }
        checkWriteAuthority(findConference.get().getEditor().getId(), jwtInfo);
    }


    private void checkWriteAuthority(UUID targetId, JwtInfo jwtInfo) {
        if (!Objects.equals(targetId, jwtInfo.getId())) {
            System.out.println("error!!!!");
            throw AuthorityException.EDITOR_ERROR;
        }
    }
}
