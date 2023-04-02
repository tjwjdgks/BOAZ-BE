package boaz.site.boazback.common.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class ObjectStorageUtilTest {

    @Spy
    private ObjectStorageUtil objectStorageUtil;

    private MultipartFile file;
    @BeforeEach
    void setUp() {

        file = new MockMultipartFile("file", "hello13.jpg", MediaType.IMAGE_PNG_VALUE, "Hello, World!".getBytes());
//       objectStorageUtil = ObjectStorageUtilTestFactory.getMinoStorage();
        objectStorageUtil = ObjectStorageUtilTestFactory.getS3Storage();
        objectStorageUtil.setup();
    }

    @Test
//    @Disabled("파일 업로드 테스트 성공")
    void upload() {
        MockMultipartFile file = new MockMultipartFile("file", "hello14.jpg", MediaType.IMAGE_PNG_VALUE, "Hello, World!".getBytes());
        String path = objectStorageUtil.upload(file, "20220122");
        System.out.println("path = " + path);
//        assertThat(path).isEqualTo("server.galbimandudev.com/test/20220122/hello13.jpeg");
    }

    @Test
    void fileNameTest(){
        List<String> filesName = objectStorageUtil.getFilesName(List.of(file));
        assertThat(file.getContentType()).isEqualTo("image/png");
        assertThat(filesName.get(0)).isEqualTo("hello13.jpg");
        assertThat(objectStorageUtil.urlPrefix()).isEqualTo("https://");
    }

    @Test
    void getFilePath(){
        String bucketName;
        String url;
        if(objectStorageUtil instanceof S3ObjectStorageUtil){
            bucketName = "<BUCKET NAME>";
            url = "https://"+bucketName+ ".s3.region-code.amazonaws.com/key-name";
        }
        else{
            bucketName = "test";
            url = "https://mino.com/"+bucketName+"/key-name";
        }
        System.out.println(url);
        String filePath = objectStorageUtil.getFilePathFormUrlByBucket(url);

        assertThat(filePath.equals("key-name")).isTrue();

    }

    @Test
    void removeFile(){
        String filePath = "testPath";
        MockMultipartFile file = new MockMultipartFile("test1", "hello15.jpg", MediaType.IMAGE_PNG_VALUE, "Hello, World!".getBytes());
        String path = objectStorageUtil.upload(file,filePath);
        System.out.println("path = " + path);
        assertThat(path.contains(filePath)).isTrue();
        objectStorageUtil.removeFile(path);
    }
    @Test
    void removeFiles(){
        String filePath = "testPath";
        MockMultipartFile file = new MockMultipartFile("test1", "hello15.jpg", MediaType.IMAGE_PNG_VALUE, "Hello, World!".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("test2", "hello15.jpg", MediaType.IMAGE_PNG_VALUE, "Hello, World!".getBytes());
        String path = objectStorageUtil.upload(file,filePath);
        String path2 = objectStorageUtil.upload(file2,filePath);
        System.out.println("path = " + path);
        System.out.println("path = " + path2);
        objectStorageUtil.removeFiles(List.of(path,path2));
    }
    @Test
    void removeFolder(){
        String filePath = "testPath";
        MockMultipartFile file = new MockMultipartFile("test1", "hello15.jpg", MediaType.IMAGE_PNG_VALUE, "Hello, World!".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("test2", "hello15.jpg", MediaType.IMAGE_PNG_VALUE, "Hello, World!".getBytes());
        String path = objectStorageUtil.upload(file,filePath);
        String path2 = objectStorageUtil.upload(file2,filePath);

        objectStorageUtil.removeFilesByFilePath(filePath);
    }
}
