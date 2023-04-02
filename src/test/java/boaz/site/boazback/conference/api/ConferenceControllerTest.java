package boaz.site.boazback.conference.api;


import boaz.site.boazback.BaseControllerTest;
import boaz.site.boazback.common.domain.DeleteFileUrlWrapper;
import boaz.site.boazback.conference.domain.Conference;
import boaz.site.boazback.conference.dto.ConferenceDto;
import boaz.site.boazback.conference.dto.ConferenceInfo;
import boaz.site.boazback.conference.dto.ConferenceUpdateInfo;
import boaz.site.boazback.user.domain.User;
import boaz.site.boazback.user.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ConferenceControllerTest extends BaseControllerTest {

    private ConferenceDto conferenceDto;
    private User fakeUser;
    private List<String> filesPath;
    List<MultipartFile> files;
    MockMultipartFile file;
    MockMultipartFile file2;

    MockMultipartFile pdfFile;
    MockMultipartFile imgFile;

    @BeforeEach
    void setUp() {
        baseSetUp();
        filesPath = new ArrayList<>();
        files = new ArrayList<>();
        file = new MockMultipartFile("files", "hello1.png", MediaType.IMAGE_PNG_VALUE, "Hello, World!".getBytes());
        file2 = new MockMultipartFile("files", "hello2.png", MediaType.IMAGE_PNG_VALUE, "Hello, World!".getBytes());
        imgFile = new MockMultipartFile("file", "test.ong", MediaType.IMAGE_PNG_VALUE, "Hello, World!".getBytes());
        pdfFile = new MockMultipartFile("file", "test.pdf", MediaType.IMAGE_PNG_VALUE, "Hello, World!".getBytes());
        files.add(file);
        files.add(file2);
        filesPath.add("1");
        filesPath.add("2");
        conferenceDto = new ConferenceDto("test", "www.naver.com", "test", "test", UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"), "www.naver.com");
        fakeUser = User.builder()
                .birthDate("0000-00-00")
                .email("example@google.com")
                .id(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"))
                .name("bob")
                .password("hello01")
                .section(3)
                .year("16")
                .build();
        UserDto userDto = UserDto.builder()
                .email("example@google.com")
                .password("hello01")
                .name("bob")
                .section(3)
                .year("16")
                .authenticationCode("")
                .birthDate("0000-00-00")
                .build();
    }


    @Test
    void 컨퍼런스등록() throws Exception {
        Conference conference = Conference.builder()
                .id(1L)
                .editor(fakeUser)
                .published(false)
                .build();
        given(conferenceService.registerConference(any())).willReturn(new ConferenceDto.InitConferenceDto(conference));
        mockMvc.perform(multipart("/conference")
                        .cookie(accessToken))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.payload.published").value(false));
    }


    @Test
    void 컨퍼런스세부내용조회() throws Exception {
        given(conferenceService.findConference(anyLong())).willReturn(conferenceDto.getConference(filesPath, fakeUser));
        mockMvc.perform(get("/conference/{uid}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.payload.title").value("test"));
    }

    @Test
    void 컨퍼런스전체페이징처리() throws Exception {
        ConferenceDto conference = new ConferenceDto("쇼미더뮤직", "www.youtube.com", "일기 쓰고 음악 추천받기", "최리,김양경", UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"), "www.naver.com");
        ConferenceDto conference2 = new ConferenceDto("쇼미더뮤직2", "www.youtube.com", "일기 쓰고 음악 추천받기2", "최리", UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"), "www.naver.com");
        List<Conference> conferenceList = new ArrayList<>();
        conferenceList.add(conference.getConference(filesPath, fakeUser));
        conferenceList.add(conference2.getConference(filesPath, fakeUser));
        List<ConferenceInfo> conferenceInfoList = conferenceList.stream().map(ConferenceInfo::new).collect(Collectors.toList());
        Page<ConferenceInfo> data = new PageImpl<>(conferenceInfoList);
        given(conferenceService.findConferencePagination(any())).willReturn(data);
        mockMvc.perform(get("/conference"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.payload.content.[1].title").value("쇼미더뮤직2"));
    }


    @Test
    void 컨퍼런스삭제() throws Exception {
        mockMvc.perform(delete("/conference/{uid}", 1)
                        .cookie(accessToken)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200));
        verify(conferenceService).deleteConference(anyLong(), any());
    }


    @Test
    void 컨퍼런스file등록하기()throws Exception{
        given(conferenceService.uploadFile(anyLong(), any(), any())).willReturn("");
        mockMvc.perform(multipart("/conference/{uid}/file", 1)
                        .file(pdfFile)
                        .cookie(accessToken))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.payload").value(""));
    }

    @Test
    void 컨퍼런스image등록하기()throws Exception{
        given(conferenceService.uploadImage(anyLong(), any(), any())).willReturn("");
        mockMvc.perform(multipart("/conference/{uid}/image", 1)
                        .file(imgFile)
                        .cookie(accessToken)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.payload").value(""));
    }


    @Test
    void 컨퍼런스image삭제하기() throws Exception {
        DeleteFileUrlWrapper deleteFileUrlWrapper = new DeleteFileUrlWrapper();
        deleteFileUrlWrapper.setFileUrl("test");
        mockMvc.perform(post("/conference/{uid}/delete-image", 1)
                        .cookie(accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteFileUrlWrapper))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload").value(true));
    }

    @Test
    void 컨퍼런스file삭제하기() throws Exception {
        DeleteFileUrlWrapper deleteFileUrlWrapper = new DeleteFileUrlWrapper();
        deleteFileUrlWrapper.setFileUrl("test");
        mockMvc.perform(post("/conference/{uid}/delete-file", 1)
                        .cookie(accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteFileUrlWrapper))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload").value(true));
    }

    @Test
    void 컨퍼런스임시상태데이터삭지() throws Exception {
        DeleteFileUrlWrapper deleteFileUrlWrapper = new DeleteFileUrlWrapper();
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("test");
        deleteFileUrlWrapper.setFileUrls(imageUrls);
        mockMvc.perform(post("/conference/{uid}/undo", 1)
                        .cookie(accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteFileUrlWrapper)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload").value(true));
    }


    @Test
        void 컨퍼런스업데이트() throws Exception {
                List<String> fileUrls = new ArrayList<>();
                fileUrls.add("www.hello.com");
                ConferenceUpdateInfo conferenceUpdateInfo = new ConferenceUpdateInfo("쇼미더뮤직", "https://www.youtube.com", "일기 쓰고 음악 추천받기", "최리,김양경", "www.naver.com", fileUrls,"www.hello.com");
                mockMvc.perform(put("/conference/{uid}",1)
                                .content(objectMapper.writeValueAsString(conferenceUpdateInfo))
                                .cookie(accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.payload").value(true));
        }

    @Test
    void 컨퍼런스업데이트_validation() throws Exception {
        List<String> fileUrls = new ArrayList<>();
        ConferenceUpdateInfo conferenceUpdateInfo = new ConferenceUpdateInfo("", "", "", "", "", fileUrls,"");
        mockMvc.perform(put("/conference/{uid}",1)
                        .content(objectMapper.writeValueAsString(conferenceUpdateInfo))
                        .cookie(accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Invalid Request Data"));
        ;
    }


    @Test
    void 컨퍼런스제목으로검색() throws Exception {
        ConferenceDto conference = new ConferenceDto("쇼미더뮤직", "www.youtube.com", "일기 쓰고 음악 추천받기", "최리,김양경", UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"), "www.naver.com");
        ConferenceDto conference2 = new ConferenceDto("쇼미더뮤직2", "www.youtube.com", "일기 쓰고 음악 추천받기2", "최리", UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"), "www.naver.com");
        List<Conference> conferenceList = new ArrayList<>();
        conferenceList.add(conference.getConference(filesPath, fakeUser));
        conferenceList.add(conference2.getConference(filesPath, fakeUser));
        List<ConferenceInfo> conferenceInfoList = conferenceList.stream().map(ConferenceInfo::new).collect(Collectors.toList());
        Page<ConferenceInfo> data = new PageImpl<>(conferenceInfoList);
        given(conferenceService.searchConferenceListForTitle(anyString(), any())).willReturn(data);
        mockMvc.perform(get("/conference/search")
                        .param("query", "test"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.payload.content.[0].title").value("쇼미더뮤직"));
    }

}
