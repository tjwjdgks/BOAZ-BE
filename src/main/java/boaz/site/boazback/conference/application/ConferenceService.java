package boaz.site.boazback.conference.application;

import boaz.site.boazback.common.domain.JwtInfo;
import boaz.site.boazback.conference.domain.Conference;
import boaz.site.boazback.conference.dto.ConferenceDto;
import boaz.site.boazback.conference.dto.ConferenceInfo;
import boaz.site.boazback.conference.dto.ConferenceUpdateInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ConferenceService {
    ConferenceDto.InitConferenceDto registerConference(JwtInfo jwtInfo);

    void deleteConference(Long uid, JwtInfo jwtInfo);

    Conference findConference(Long uid);

    int countAllConference();

    boolean updateConference(Long uid, ConferenceUpdateInfo conferenceUpdateInfo, JwtInfo jwtInfo);

    Page<ConferenceInfo> findConferencePagination(Pageable pageable);

    Page<ConferenceInfo> searchConferenceListForTitle(String title,Pageable pageable);

    String uploadImage(Long conferenceId, JwtInfo jwtInfo, MultipartFile imgFile);

    void deleteConferenceImage(Long conferenceId, JwtInfo jwtInfo, String imageUrl);

    void undoConference(Long conferenceId, JwtInfo jwtInfo, List<String> imageUrlList);

    String uploadFile(Long conferenceId, JwtInfo jwtInfo, MultipartFile file);

    void deleteConferenceFile(Long conferenceId, JwtInfo jwtInfo, String fileUrl);
}
