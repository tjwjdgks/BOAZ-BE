package boaz.site.boazback.question.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QuestionTest {

    @Test
    void builder() {
        Question question = Question.builder()
                .detail("test")
                .email("example@naver.com")
                .name("bob")
                .title("test")
                .build();
        assertThat(question.getName()).isEqualTo("bob");
    }
}
