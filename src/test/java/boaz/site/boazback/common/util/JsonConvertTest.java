package boaz.site.boazback.common.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@JsonTest
public class JsonConvertTest {

     static class TestUUID{
        private UUID userId;
        public TestUUID() {}

        public TestUUID(UUID userId) {
            this.userId = userId;
        }

        public UUID getUserId() {
            return userId;
        }
    }

    static class TestLocalDateTime{
        @JsonFormat(pattern ="uuuu-MM-dd'T'HH:mm:ss")
        private LocalDateTime localDateTime;
        public TestLocalDateTime() {}

        public TestLocalDateTime(LocalDateTime localDateTime) {
            this.localDateTime = localDateTime;
        }

        public LocalDateTime getLocalDateTime() {
            return localDateTime;
        }
    }

    @Autowired
    private JacksonTester<TestUUID> jsonUUID;

    @Autowired
    private JacksonTester<TestLocalDateTime> jsonLocalDateTime;

    private TestUUID testUUID;
    private TestLocalDateTime testLocalDateTime;
    private String contentUUID ="";
    private String contentLocalDateTime ="";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss");
    @BeforeEach
    void setUp(){
        testUUID= new TestUUID(UUID.randomUUID());
        testLocalDateTime = new TestLocalDateTime(LocalDateTime.now());
        contentUUID = "{\"userId\":\""+testUUID.getUserId().toString()+"\"}";
        contentLocalDateTime = "{\"localDateTime\":\""+testLocalDateTime.getLocalDateTime().format(formatter)+"\"}";
    }


    @Test
    @DisplayName("uuid-> string toString 테스트")
    void uuidToString() throws IOException {
        assertThat(jsonUUID.write(testUUID).getJson()).isEqualTo(contentUUID);
    }
    @Test
    @DisplayName("string -> uuid fromString 테스트")
    void stringToUUID() throws IOException {
        assertThat(jsonUUID.parseObject(contentUUID).getUserId()).isEqualTo(testUUID.getUserId());
    }
    @Test
    @DisplayName("equals uuid test")
    void equalsUUID(){
        UUID uuid1 = UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66");
        UUID uuid2 = UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66");
        assertThat(uuid1).isEqualTo(uuid2);
    }

    @Test
    @DisplayName("localDateTime -> json")
    void localDateTImeToJson() throws IOException {

        assertThat(jsonLocalDateTime.write(testLocalDateTime).getJson()).isEqualTo(contentLocalDateTime);

    }
    @Test
    @DisplayName("json -> localDateTime")
    void jsonToLocalDateTime() throws IOException {
        assertThat(jsonLocalDateTime.parseObject(contentLocalDateTime).getLocalDateTime())
                .isEqualTo(testLocalDateTime.getLocalDateTime().format(formatter));
    }
}
