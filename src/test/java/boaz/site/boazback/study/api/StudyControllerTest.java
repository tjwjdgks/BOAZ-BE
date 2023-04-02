package boaz.site.boazback.study.api;


import boaz.site.boazback.BaseControllerTest;
import boaz.site.boazback.study.domain.Study;
import boaz.site.boazback.study.domain.StudyCategory;
import boaz.site.boazback.study.dto.StudyDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



class StudyControllerTest extends BaseControllerTest {

    StudyDto fakeStudyDto;
    Study fakeStudy;
    Study fakeStudy2;
    Study fakeStudy3;
    List<Study> fakeStudyList;
    StudyCategory studyCategory;
    MockMultipartFile file;

    @Autowired
    ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        baseSetUp();
        file = new MockMultipartFile("file", "hello1.png", MediaType.IMAGE_PNG_VALUE, "Hello, World!".getBytes());
        studyCategory = new StudyCategory(1L, "analysis");
        fakeStudyDto = StudyDto.builder()
                .editor(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"))
                .contents("test")
                .studyCategoryId(1L)
                .title("test")
                .build();
        fakeStudy = Study.builder()
                .id(1L)
                .contents("test")
                .studyCategory(studyCategory)
                .imgUrl(new ArrayList<>())
                .title("test")
                .build();
        fakeStudy2 = Study.builder()
                .id(2L)
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
    void 스터티최초등록하기() throws Exception {
        // registerStudy
        given(studyService.registerStudy(any())).willReturn(fakeStudy);
        given(userService.isExistUser(any())).willReturn(true);
        mockMvc.perform(post("/study")
                        .param("studyCategoryId","1")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .cookie(accessToken))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.payload.id").value("1"));
    }


    @Test
    void 스터디세부사항요청하기() throws Exception {
        // findStudy
        given(studyService.findStudy(anyLong())).willReturn(fakeStudy);
        mockMvc.perform(get("/study/detail/{uid}", 1)
                .cookie(accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.payload.id").value("1"))
                .andExpect(jsonPath("$.payload.contents").value("test"));
    }


    @Test
    void 스터디리스트요청하기() throws Exception {
        // findStudyInventory
        List<Study> data = fakeStudyList.stream()
                .sorted(Comparator.comparing(Study::getId, Comparator.reverseOrder())).collect(Collectors.toList());
        given(studyService.findStudies(anyLong(), any())).willReturn(data);
        mockMvc.perform(get("/study/{uid}", 1)
                        .param("size", "2")
                        .param("page", "0")
                .cookie(accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.[0].id").value(3));

    }

    @Test
    void 스터디업데이트() throws Exception{
        // updateStudy
        given(studyService.updateStudy(anyLong(),any(),any())).willReturn(fakeStudy);
        mockMvc.perform(put("/study/1")
                        .param("title","스터디등록")
                        .param("studyCategoryId","1")
                        .param("contents","스터디원 모집합니다.")
                        .param("editor","df8b21f0-c2d6-11ec-a6d6-0800200c9a66")
                        .cookie(accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.payload.id").value("1"))
                .andExpect(jsonPath("$.payload.contents").value("test"));
    }
    @Test
    void 스터디업데이트유효성Null() throws Exception{
        // updateStudy
        given(studyService.updateStudy(anyLong(),any(),any())).willReturn(fakeStudy);
        mockMvc.perform(put("/study/1")
                .cookie(accessToken))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }
    @Test
    void 스터디업데이트유효성Empty() throws Exception{
        // updateStudy
        given(studyService.updateStudy(anyLong(),any(),any())).willReturn(fakeStudy);
        mockMvc.perform(put("/study/1")
                .param("title","")
                .param("studyCategoryId","1")
                .param("contents","")
                .param("editor","df8b21f0-c2d6-11ec-a6d6-0800200c9a66")
                .cookie(accessToken))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }
    @Test
    void 스터디수정취소하기() throws Exception{
        // undoStudy
        mockMvc.perform(post("/study/{uid}/undo",1)
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(accessToken)
                .content("{\"imageUrls\":[\"http://test.com/test.jpg\",\"http://test.com/test1.jpg\",\"http://test.com/test2.jpg\"]}")
            )
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void 스터디삭제하기() throws Exception {
        // deleteStudy
        mockMvc.perform(delete("/study/{studyId}/", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessToken)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 페이지갯수보내기() throws Exception {
        // getStudyTotalPages
        given(studyService.getTotalPages(anyLong(), anyLong())).willReturn(1L);
        mockMvc.perform(get("/study/page")
                        .param("size", "2")
                        .param("studyCategoryId","1")
                    .cookie(accessToken)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload").value(1));
    }

    @Test
    void 스터디파일업로드() throws Exception {
        given(studyService.upload(any(),anyString())).willReturn("test@test.com");
        mockMvc.perform(multipart("/study/{studyId}/file",1)
            .file("file",file.getBytes())
                .cookie(accessToken))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.payload").value("test@test.com"));
    }
    @Test
    void 스터디파일유효성() throws Exception {
        given(studyService.upload(any(),anyString())).willReturn("test@test.com");
        mockMvc.perform(multipart("/study/{studyId}/file",1)
                .cookie(accessToken))
            .andExpect(status().isBadRequest());
    }
}
