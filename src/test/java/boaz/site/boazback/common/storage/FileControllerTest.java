package boaz.site.boazback.common.storage;

import boaz.site.boazback.BaseControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest({FileController.class})
class FileControllerTest extends BaseControllerTest {

    @Autowired
    MockMvc mockMvc;

    MockMultipartFile file;

    @BeforeEach
    void setUp(){
        baseSetUp();
        file = new MockMultipartFile("file", "hello1.png", MediaType.IMAGE_PNG_VALUE, "Hello, World!".getBytes());
    }

    @Test
    @DisplayName("file 업로드 test : study")
    void studyUpload() throws Exception {
        given(objectStorageUtil.upload(any(),anyString())).willReturn("test");
        mockMvc.perform(multipart("/file/study/{seq}/upload",1)
                .file("file",file.getBytes())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .cookie(accessToken))
            .andDo(print())
            .andExpect(status().isOk());
    }
    @Test
    @DisplayName("file 업로드 test : conference")
    void conferenceUpload() throws Exception {
        given(objectStorageUtil.upload(any(),anyString())).willReturn("test");
        mockMvc.perform(multipart("/file/conference/{seq}/upload",1)
                        .file("file",file.getBytes())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .cookie(accessToken))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
