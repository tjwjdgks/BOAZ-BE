package boaz.site.boazback.user.domain;

import boaz.site.boazback.user.dto.UserDto;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDtoTest {


    @Test
    void builder(){
        UserDto userDto = UserDto.builder().build();
        User user = userDto.transForm();
        assertThat(user.getName()).isNull();

    }
    @DisplayName("User dto validation 테스트")
    @Nested
    static class UserDtoValidationTest{
        private static Validator validator;
        @BeforeAll
        public static void setUp(){
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        }

        @Test
        @DisplayName("UserDto validation 성공")
        void validationSuccess(){
            UserDto userDto = UserDto.builder()
                    .email("example@naver.com")
                    .password("hello01")
                    .name("bob")
                    .section(3)
                    .year("16")
                    .authenticationCode("")
                    .birthDate("0000-00-00")
                    .build();
            Set<ConstraintViolation<UserDto>> validate = validator.validate(userDto);
            assertTrue(validate.isEmpty());
        }
        @Test
        @DisplayName("UserDto validation 실패")
        void validationFail(){
            UserDto userDto = UserDto.builder()
                    .password("hello01")
                    .name("bob")
                    .section(3)
                    .year("16")
                    .authenticationCode("")
                    .birthDate("0000-00-00")
                    .build();
            Set<ConstraintViolation<UserDto>> validate = validator.validate(userDto);
            assertFalse(validate.isEmpty());
        }
    }



}
