package boaz.site.boazback.conference.application;

import boaz.site.boazback.common.domain.JwtInfo;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ConferenceServiceTest {

    @Mock
    private ConferenceRepository conferenceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ObjectStorageUtil objectStorageUtil;


    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private EntityManager entityManager;

    @Mock
    private ConferenceQueryRepositoryImpl conferenceQueryRepository;

    private ConferenceService conferenceService;

    private ConferenceDto conferenceDto;
    private List<String> filesPath;
    private User fakeUser;
    List<MultipartFile> files;
    MockMultipartFile file;
    MockMultipartFile file2;
    JwtInfo jwtInfo;

    private Long fakeConferenceId = 1L;
    @BeforeEach
    void setup() {
        filesPath = new ArrayList<>();
        conferenceService = new ConferenceServiceImpl(conferenceRepository, userRepository,objectStorageUtil,entityManager,conferenceQueryRepository);
        files = new ArrayList<>();
        file = new MockMultipartFile("file", "hello1.png", MediaType.IMAGE_PNG_VALUE, "Hello, World!".getBytes());
        file2 = new MockMultipartFile("file", "hello2.png", MediaType.IMAGE_PNG_VALUE, "Hello, World!".getBytes());
        files.add(file);
        files.add(file2);
        filesPath.add("1");
        filesPath.add("2");
        conferenceDto = new ConferenceDto("test", "www.naver.com", "test", "test", UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"),"www.naver.com");
        jwtInfo = new JwtInfo(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"),1,"taebin","example@example.com","16","16_analysis_taebin",0);
        fakeUser = User.builder()
                .birthDate("0000-00-00")
                .email("example@naver.com")
                .id(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"))
                .name("bob")
                .password("hello01")
                .section(3)
                .year("16")
                .build();

    }

    @Test
    void 컨퍼런스등록하기() {
        given(userRepository.findById(any())).willReturn(Optional.of(fakeUser));
        given(conferenceRepository.save(any())).willReturn(conferenceDto.getConference(filesPath, fakeUser));
        ConferenceDto.InitConferenceDto result = conferenceService.registerConference(jwtInfo);
        assertThat(result.isPublished()).isFalse();
        verify(conferenceRepository).save(any());
    }

    @Test
    void 컨퍼런스등록하기유저없음(){
        given(userRepository.findById(any())).willReturn(Optional.empty());
        Throwable error = assertThrows(UserException.class, () -> conferenceService.registerConference(jwtInfo));
        assertThat(error.getMessage()).isEqualTo("user not found");
    }

    @Test
    void 컨펀런스세부내용보기() {
        given(conferenceRepository.findByIdAndPublished(anyLong(),anyBoolean())).willReturn(Optional.ofNullable(conferenceDto.getConference(filesPath, fakeUser)));
        Conference result = conferenceService.findConference(fakeConferenceId);
        assertThat(result.getTitle()).isEqualTo("test");
        assertThat(result.getEditor().getId()).isEqualTo(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"));
    }

    @Test
    void 컨퍼런스페이징처리생성기준내림차순() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Conference> data = new PageImpl<>(new ArrayList<>());
        given(conferenceRepository.findByPublishedOrderByCreatedDateDesc(anyBoolean(),any())).willReturn(data);
        Page<ConferenceInfo> result = conferenceService.findConferencePagination(pageable);
        assertThat(result).isEmpty();
    }

    @Test
    void 컨퍼런스전체갯수처리() {
        given(conferenceRepository.findByPublished(anyBoolean())).willReturn(new ArrayList<>());
        int cnt = conferenceService.countAllConference();
        assertThat(cnt).isZero();
    }


    @Test
    void 컨퍼런스삭제하기() {
        createFakeConference();
        conferenceService.deleteConference(fakeConferenceId, jwtInfo);
        verify(conferenceRepository).deleteById(anyLong());
        verify(objectStorageUtil).removeFilesByFilePath(anyString());
    }

    @Test
    void 컨퍼런스가존재하지않음(){
        conferenceDto.setEditorId(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"));
        given(conferenceRepository.findById(anyLong())).willReturn(Optional.empty());
        Throwable error = assertThrows(ConferenceException.class,() ->conferenceService.deleteConference(2L, jwtInfo));
        assertThat(error.getMessage()).isEqualTo("Conference not found");
    }


    @Test
    void 컨퍼런스삭제권한없음(){
        conferenceDto.setEditorId(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"));
        jwtInfo.setId(UUID.randomUUID());
        Conference conference = conferenceDto.getConference(filesPath, fakeUser);
        given(conferenceRepository.findById(anyLong())).willReturn(Optional.of(conference));
        Throwable error = assertThrows(AuthorityException.class,() ->conferenceService.deleteConference(2L, jwtInfo));
        assertThat(error.getMessage()).isEqualTo("you are not admin or Editor");
    }

    @Test
    void 컨퍼러스업데이트하기(){
        List<String> fileUrls = new ArrayList<>();
        fileUrls.add("as");
        ConferenceUpdateInfo conferenceUpdateInfo = new ConferenceUpdateInfo("test", "www.naver.com", "test", "test", "www.naver.com",fileUrls,"");
        given(conferenceRepository.findById(anyLong())).willReturn(Optional.ofNullable(conferenceDto.getConference(filesPath, fakeUser)));
        given(conferenceRepository.save(any())).willReturn(conferenceDto.getConference(filesPath, fakeUser));
        boolean result = conferenceService.updateConference(fakeConferenceId,conferenceUpdateInfo ,jwtInfo);
        assertThat(result).isTrue();
        verify(conferenceRepository).save(any());
    }

    @Test
    void 컨퍼러스업데이트하기_컨퍼런스가없음(){
        List<String> fileUrls = new ArrayList<>();
        fileUrls.add("as");
        ConferenceUpdateInfo conferenceUpdateInfo = new ConferenceUpdateInfo("test", "www.naver.com", "test", "test", "www.naver.com",fileUrls,"");
        given(conferenceRepository.findById(anyLong())).willReturn(Optional.empty());
        Throwable error = assertThrows(ConferenceException.class,() ->conferenceService.updateConference(fakeConferenceId,conferenceUpdateInfo ,jwtInfo));
        assertThat(error.getMessage()).isEqualTo("Conference not found");
    }


    @Test
    void 컨퍼런스검색하기(){
        Pageable pageable = PageRequest.of(0, 2);
        Conference conference = conferenceDto.getConference(filesPath, fakeUser);
        ConferenceInfo conferenceInfo = new ConferenceInfo(conference);
        List<Conference> conferenceList = new ArrayList<>();
        conferenceList.add(conference);
        Page<Conference> data = new PageImpl<>(conferenceList);
        given(conferenceQueryRepository.findConferenceByTitleAndWriterOrderByCreatedDateDesc(any(),anyString())).willReturn(data);
        Page<ConferenceInfo> conferenceInfoPage = conferenceService.searchConferenceListForTitle("test", pageable);
        assertThat(conferenceInfoPage.getTotalElements()).isEqualTo(1);
    }


    @Test
    void 이미지업로드하기(){
        createFakeConference();
        given(objectStorageUtil.upload((MultipartFile) any(), anyString())).willReturn("ttt");
        String result = conferenceService.uploadImage(fakeConferenceId, jwtInfo, file);
        assertThat(result).isEqualTo("ttt");
    }

    @Test
    void 이미지삭제하기(){
        createFakeConference();
        conferenceService.deleteConferenceImage(fakeConferenceId,jwtInfo,"test");
        verify(objectStorageUtil).removeFile(anyString());
    }

    @Test
    void 파일업로드하기(){
        createFakeConference();
        given(objectStorageUtil.upload((MultipartFile) any(), anyString())).willReturn("ttt");
        String result = conferenceService.uploadFile(fakeConferenceId, jwtInfo, file);
        assertThat(result).isEqualTo("ttt");
    }

    @Test
    void 파일삭제하기(){
        createFakeConference();
        conferenceService.deleteConferenceFile(fakeConferenceId,jwtInfo,"test");
        verify(objectStorageUtil).removeFile(anyString());
    }

    @Test
    void 임시컨퍼런스삭제하기(){
        createFakeConference();
        List<String> imageUrlList = new ArrayList<>();
        imageUrlList.add("test");
        conferenceService.undoConference(fakeConferenceId,jwtInfo,imageUrlList);
        verify(conferenceRepository).deleteById(anyLong());
        verify(objectStorageUtil).removeFiles(anyList());
    }

    private void createFakeConference() {
        Conference conference = conferenceDto.getConference(filesPath, fakeUser);
        given(conferenceRepository.findById(anyLong())).willReturn(Optional.of(conference));
    }

}
