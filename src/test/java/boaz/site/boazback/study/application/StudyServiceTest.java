package boaz.site.boazback.study.application;

import boaz.site.boazback.common.domain.JwtInfo;
import boaz.site.boazback.common.storage.ObjectStorageUtil;
import boaz.site.boazback.study.domain.Study;
import boaz.site.boazback.study.domain.StudyCategory;
import boaz.site.boazback.study.dto.StudyDto;
import boaz.site.boazback.user.application.UserRepository;
import boaz.site.boazback.user.domain.User;
import boaz.site.boazback.user.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {


    @Mock
    private StudyRepository studyRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private StudyCategoryRepository studyCategoryRepository;

    @Mock
    private ObjectStorageUtil objectStorageUtil;

    private StudyService studyService;

    UserDto fakeUserDto;
    User fakeUser;
    StudyDto fakeStudyDto;
    Study fakeStudy;
    Study fakeStudy2;
    Study fakeStudy3;
    List<Study> fakeStudyList;
    StudyCategory studyCategory;
    List<MultipartFile> files;
    MockMultipartFile file;
    MockMultipartFile file2;
    JwtInfo jwtInfo;

    @BeforeEach
    void setup() {

        studyService = new StudyServiceImpl(studyRepository, userRepository, studyCategoryRepository, objectStorageUtil);

        files = new ArrayList<>();
        file = new MockMultipartFile("file", "hello1.png", MediaType.IMAGE_PNG_VALUE, "Hello, World!".getBytes());
        file2 = new MockMultipartFile("file", "hello2.png", MediaType.IMAGE_PNG_VALUE, "Hello, World!".getBytes());
        files.add(file);
        files.add(file2);

        fakeUserDto = UserDto.builder()
                .email("example@gmail.com")
                .password("hello01")
                .name("bob")
                .section(3)
                .year("16")
                .authenticationCode("helloboaz")
                .birthDate("0000-00-00")
                .build();

        fakeUser = User.builder()
                .email("example@gmail.com")
                .password("hello01")
                .name("bob")
                .section(3)
                .year("16")
                .birthDate("0000-00-00")
                .id(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"))
                .build();

        jwtInfo = new JwtInfo(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"),3,"bob","example@naver.com","16","bob",1);


        studyCategory = new StudyCategory(1L, "analysis");
        fakeStudyDto = StudyDto.builder()
                .editor(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"))
                .contents("test")
                .studyCategoryId(1L)
                .title("test")
                .imgUrl(new ArrayList<>())
                .build();

        fakeStudy = Study.builder()
                .id(1L)
                .editor(fakeUser)
                .contents("test")
                .studyCategory(studyCategory)
                .imgUrl(new ArrayList<>(List.of("test1","test2")))
                .title("test")
                .build();
        fakeStudy2 = Study.builder()
                .id(2L)
                .editor(fakeUser)
                .studyCategory(studyCategory)
                .contents("test2")
                .imgUrl(new ArrayList<>())
                .title("test2")
                .build();
        fakeStudy3 = Study.builder()
                .id(3L)
                .studyCategory(new StudyCategory(2L, "analysis"))
                .contents("test3")
                .imgUrl(new ArrayList<>())
                .title("test3")
                .build();
        fakeStudyList = new ArrayList<>();
        fakeStudyList.add(fakeStudy);
        fakeStudyList.add(fakeStudy2);
        fakeStudyList.add(fakeStudy3);
    }

    @Test
    @DisplayName("빈 study 등록하기")
    void registerStudy() {
        given(userRepository.findById(any())).willReturn(Optional.of(fakeUserDto.transForm()));
        given(studyRepository.save(any())).willReturn(fakeStudy2);

        Study study = studyService.registerStudy(fakeUser.getId());
        System.out.println("study = " + study);
        assertThat(study).isNotNull();
        assertThat(study.getImgUrl()).isEmpty();
    }

    @Test
    @DisplayName("실제 study 등록하기-editor가 없을 경우")
    void registerStudyNotEditor() {
        given(userRepository.findById(any())).willReturn(Optional.empty());
        Throwable error = assertThrows(IllegalStateException.class, () -> studyService.registerStudy(null));
        assertThat(error.getMessage()).isEqualTo("editor not exist");
    }


    @Test
    @DisplayName("스터디 세부내용 조회하기")
    void findStudy() {
        given(studyRepository.findById(anyLong())).willReturn(Optional.of(fakeStudy));
        Study study = studyService.findStudy(1L);
        assertThat(study).isNotNull();
        assertThat(study.getTitle()).isEqualTo("test");
    }

    @Test
    @DisplayName("스터디 세부내용 조회 시 - 스터티가 없을 경우")
    void findStudyError1() {
        given(studyRepository.findById(anyLong())).willReturn(Optional.empty());
        Throwable error = assertThrows(IllegalStateException.class, () -> studyService.findStudy(1L));
        assertThat(error.getMessage()).isEqualTo("study not exist");
    }

    @Test
    @DisplayName("스터디 목록 조회하기 - 공지")
    void findStudiesId2() {
        given(studyCategoryRepository.findById(anyLong())).willReturn(Optional.of(studyCategory));
        Pageable pageable = PageRequest.of(0, 2);
        Study fakeStudy1 = Study.builder()
                .title("test1")
                .build();
        List<Study> data = fakeStudyList.stream()
                .filter(study -> study.getStudyCategory().getId() == 1L)
                .sorted(Comparator.comparing(Study::getId, Comparator.reverseOrder())).collect(Collectors.toList());
        given(studyRepository.findByStudyCategory_IdAndPublishedIsTrueOrderByModifiedDateDesc(anyLong(), any())).willReturn(data);
        List<Study> result = studyService.findStudies(2L, pageable);
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(2L);

    }

    @Test
    @DisplayName("스터디 목록 조회하기 - 전체")
    void findStudiesId1() {
        Pageable pageable = PageRequest.of(0, 2);
        given(studyCategoryRepository.findById(anyLong())).willReturn(Optional.of(studyCategory));
        List<Study> data = fakeStudyList.stream()
                .sorted(Comparator.comparing(Study::getId, Comparator.reverseOrder())).collect(Collectors.toList());
        given(studyRepository.findByPublishedIsTrueOrderByModifiedDateDesc(any())).willReturn(data);
        List<Study> result = studyService.findStudies(0L, pageable);
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getId()).isEqualTo(3L);
    }
    @Test
    @DisplayName("스터디 수정 이미지 없는 경우 ")
    void updateStudyWithNoImage(){
        fakeStudyDto.setImgUrl(new ArrayList<>());
        given(studyRepository.findById(anyLong())).willReturn(Optional.of(fakeStudy));
        Study study = studyService.updateStudy(1L, fakeStudyDto, jwtInfo);

        List<String> imgUrl = study.getImgUrl();
        assertThat(imgUrl.size()).isEqualTo(0);

    }
    @Test
    @DisplayName("스터디 수정 카테고리 없는 경우 ")
    void updateStudy(){
        fakeStudyDto.setStudyCategoryId(null);
        given(studyRepository.findById(anyLong())).willReturn(Optional.of(fakeStudy));
        given(studyCategoryRepository.findById(anyLong())).willReturn(Optional.of(studyCategory));
        Study study = studyService.updateStudy(1L, fakeStudyDto, jwtInfo);
        verify(studyCategoryRepository).findById(0L);
    }
    @Test
    @DisplayName("스터디 수정 카테고리 다른 경우 ")
    void updateStudyCategory(){
        assertThat(fakeStudyDto.getStudyCategoryId()).isNotEqualTo(3l);
        fakeStudyDto.setStudyCategoryId(3L);
        given(studyRepository.findById(anyLong())).willReturn(Optional.of(fakeStudy));
        given(studyCategoryRepository.findById(anyLong())).willReturn(Optional.of(studyCategory));
        Study study = studyService.updateStudy(1L, fakeStudyDto, jwtInfo);
        verify(studyCategoryRepository).findById(3l);
    }

    @Test
    @DisplayName("스터디 삭제")
    void deleteStudy() {
        given(studyRepository.findById(anyLong())).willReturn(Optional.of(fakeStudy));
        studyService.deleteStudy(1L,jwtInfo);
        verify(studyRepository).delete(any());
    }

    @Test
    @DisplayName("스터디 수정 취소")
    void undoStudy(){
        given(studyRepository.findById(anyLong())).willReturn(Optional.of(fakeStudy));
        studyService.undoStudy(1L,jwtInfo,List.of("https://test.com/test","https://test.com/test2"));
        verify(objectStorageUtil).removeFiles(any());
    }

    @Test
    @DisplayName("스터디 총 갯수 세기")
    void getTotalPages(){
        given(studyRepository.countTotalStudy(anyLong())).willReturn(0L);
        Long result = studyService.getTotalPages(2L, 1L);
        assertThat(result).isEqualTo(1L);
    }

}
