package boaz.site.boazback.question.domain;

import boaz.site.boazback.question.dto.QuestionDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class QuestionDtoTest {

    @DisplayName("빌더 테스트 하기")
    @Test
    void builder() {
        QuestionDto questionDto = QuestionDto.builder()
                .detail("test")
                .email("test")
                .email("example@naver.com")
                .name("bob")
                .build();
        assertThat(questionDto.getEmail()).isEqualTo("example@naver.com");
    }


    @DisplayName("dto를 도메인으로 변경하기")
    @Test
    void transForm() {
        QuestionDto questionDto = QuestionDto.builder()
                .detail("test")
                .email("test")
                .email("example@naver.com")
                .name("bob")
                .build();
        Question question = questionDto.transForm();
        assertThat(question).isNotInstanceOf(QuestionDto.class);
        assertThat(question.getDetail()).isEqualTo("test");
    }

    @DisplayName("Question dto validation 테스트")
    @Nested
    static class QuestionDtoValidationTest{
        private static Validator validator;
        @BeforeAll
        public static void setUp() {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        }
        @Test
        @DisplayName("QuestionDto validation 성공")
        void validationSuccess(){
            QuestionDto questionDto = QuestionDto.builder()
                    .detail("test")
                    .title("test")
                    .email("example@naver.com")
                    .name("bob")
                    .build();
            Set<ConstraintViolation<QuestionDto>> validations = validator.validate(questionDto);
            assertTrue(validations.isEmpty());
        }
        @Test
        @DisplayName("QuestionDto validation 실패")
        void validationFail(){
            QuestionDto questionDto = QuestionDto.builder()
                    .detail("test")
                    .email("example@naver.com")
                    .name("bob")
                    .build();
            Set<ConstraintViolation<QuestionDto>> validations = validator.validate(questionDto);
            assertFalse(validations.isEmpty());

        }
    }


}
