package boaz.site.boazback.user.application;


import boaz.site.boazback.common.exception.CertificationException;
import boaz.site.boazback.common.exception.UserException;
import boaz.site.boazback.common.util.CertificationUtil;
import boaz.site.boazback.common.util.MailUtil;
import boaz.site.boazback.email.application.EmailRepository;
import boaz.site.boazback.email.domain.Email;
import boaz.site.boazback.email.domain.EmailType;
import boaz.site.boazback.email.event.EmailJoinEvent;
import boaz.site.boazback.user.dto.FindEmailDto;
import boaz.site.boazback.user.domain.User;
import boaz.site.boazback.user.dto.UserDto;
import boaz.site.boazback.user.dto.UserLogin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailRepository emailRepository;

    @Mock
    private CertificationUtil certificationUtil;

    @Mock
    private MailUtil mailUtil;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private UserService userService;

    private UserDto fakeUserDto;
    private Email email;
    private User fakeUser;


    UserLogin userLogin;

    @BeforeEach
    void setUp() {

        //service setting

        userService = new UserServiceImpl(userRepository, emailRepository, certificationUtil, mailUtil,eventPublisher);

        //fake data setting
        fakeUserDto = UserDto.builder()
                .email("example@gmail.com")
                .password("hello01")
                .name("bob")
                .section(3)
                .year("16")
                .authenticationCode("")
                .birthDate("0000-00-00")
                .build();
        userLogin = new UserLogin("example@naver.com", "hello01");
        fakeUser = fakeUserDto.transForm();
        fakeUser = fakeUser.certificateEmail();

        email = Email.builder()
                .id(1L)
                .authCode("test")
                .userEmail("example@naver.com")
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now().minusHours(2))
                .build();
    }

    @Test
    @DisplayName("유저를 등록합니다.")
    void registerUser() {
        //given
        fakeUserDto.setPassword("");
        given(userRepository.save(any())).willReturn(fakeUserDto.transForm());
        User result = userService.registerUser(fakeUserDto);
        assertThat(result.getPassword()).isNotEqualTo("hello01"); //integration 할
        assertThat(result.getEditName()).isEqualTo("16_엔지니어링_bob");
    }

    @Test
    @DisplayName("유저를 등록하는데 이미 있을 경우")
    void registerUser2() {
        //given
        fakeUserDto.setPassword("123");
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(fakeUserDto.transForm()));
        Throwable error = assertThrows(UserException.class, () -> userService.registerUser(fakeUserDto));
        assertThat(error.getMessage()).isEqualTo("user already registered");
    }

    @Test
    @DisplayName("유저를 등록하는데 인증번호를 틀린 경우")
    void registerUser3() {
        //given
        fakeUserDto.setAuthenticationCode("boaz");
        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());
        Throwable error = assertThrows(CertificationException.class, () -> userService.registerUser(fakeUserDto));
        assertThat(error.getMessage()).isEqualTo("CertificationCode is not same");
    }


    @Test
    @DisplayName("유저를 삭제합니다.")
    void deleteUser() {
        Long uid = 1L;
        userService.deleteUser(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"));
        verify(userRepository).deleteById(any());
    }


    @Test
    @DisplayName("유저가 로그인하다")
    void loginUser() {
        fakeUserDto.setPassword("");
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(fakeUserDto.transForm()));
        User loginUser = userService.loginUser(userLogin);
        assertThat(loginUser).isNotNull();
        assertThat(loginUser.getEditName()).isEqualTo("16_엔지니어링_bob");
    }

    @Test
    @DisplayName("유저가 로그인을 하는데  유저가 없는 경우")
    void loginUserNotExists() {
        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());
        Throwable err = assertThrows(UserException.class, () -> userService.loginUser(userLogin));
        assertThat(err.getMessage()).isEqualTo("you are not user");
    }

    @Test
    @DisplayName("유저가 로그인을 하는데 비밀번호가 틀린 경우")
    void loginUserUnCorrectPassword() {
        fakeUserDto.setPassword("");
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(fakeUserDto.transForm()));
        userLogin.setPassword("hello02");
        Throwable err = assertThrows(UserException.class, () -> userService.loginUser(userLogin));
        assertThat(err.getMessage()).isEqualTo("password not correct");
    }

    @Test
    @DisplayName("유저가 이메일을 클릭해서 승인으로 변경할 때")
    void certificationSignupConfirm() {
        String encodedEmail = "";
        given(emailRepository.findByUserEmailAndEmailType(anyString(), any(EmailType.class))).willReturn(Optional.of(email));
        doNothing().when(certificationUtil).checkExpiredCertificationCode(any());
        doNothing().when(certificationUtil).checkCertificationCode(anyString(), anyString());
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(fakeUserDto.transForm()));
        userService.certificationSignupConfirm("test", encodedEmail);
        verify(emailRepository).deleteById(anyLong());
        verify(userRepository).save(any());
    }

    @Test
    @DisplayName("유저가 이메일을 클릭해서 승인으로 변경할 때 - 유저 못참음")
    void emailCheckUserNotFoundUser() {
        String certificateCode = "";
        String encodedEmail = "";
        given(emailRepository.findByUserEmailAndEmailType(anyString(), any(EmailType.class))).willReturn(Optional.of(email));
        doNothing().when(certificationUtil).checkExpiredCertificationCode(any());
        doNothing().when(certificationUtil).checkCertificationCode(anyString(), anyString());
        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());
        Throwable error = assertThrows(UserException.class, () -> userService.certificationSignupConfirm(certificateCode, encodedEmail));
        assertThat(error.getMessage()).isEqualTo("user not found");
    }

    @Test
    @DisplayName("유저가 이메일을 클릭해서 승인으로 변경할 때 - 이미 승인 되어 있을 경우")
    void certificationSignupConfirmAlreadyCheck() {
        String encodedEmail = "";
        given(emailRepository.findByUserEmailAndEmailType(anyString(), any(EmailType.class))).willReturn(Optional.of(email));
        doNothing().when(certificationUtil).checkExpiredCertificationCode(any());
        doNothing().when(certificationUtil).checkCertificationCode(anyString(), anyString());
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(fakeUser));
        Throwable error = assertThrows(UserException.class, () -> userService.certificationSignupConfirm("test", encodedEmail));
        assertThat(error.getMessage()).isEqualTo("user already certificated");
    }


    @Test
    void 이메일인코딩디코딩hex로변환() {
        String str = "example@naver.com";
        byte[] getBytesFromString = str.getBytes(StandardCharsets.UTF_8);
        BigInteger bigInteger = new BigInteger(1, getBytesFromString);
        String convertedResult = String.format("%x", bigInteger);
        System.out.println("Converted Hex from String: " + convertedResult);
        BigInteger r = new BigInteger("", 16);
        byte[] a = r.toByteArray();
        String result = new String(a, StandardCharsets.UTF_8);
        System.out.println("Converted String from Hex: " + result);
    }

    @Test
    void 유저비밀번호새로발급받기() {
        FindEmailDto findEmailDto = new FindEmailDto();
        userService.reissueUserPasswordByEmail(findEmailDto);
    }


    @Test
    void 회원가입관련이메일재발급받기() {
        String senderEmail = "";
        given(emailRepository.findByUserEmailAndEmailType(anyString(), any(EmailType.class))).willReturn(Optional.of(email));
        given(certificationUtil.createCertificationCode()).willReturn("t");
        doNothing().when(eventPublisher).publishEvent(any(EmailJoinEvent.class));
        userService.resendJoinConfirmMail(senderEmail);
        verify(certificationUtil).createCertificationCode();
        verify(eventPublisher,times(1)).publishEvent(any(EmailJoinEvent.class));
    }

    @Test
    void findUser() {

    }

    @Test
    void isExistUser() {
        given(userRepository.findById(any(UUID.class))).willReturn(Optional.empty());
        boolean result = userService.isExistUser(UUID.randomUUID());
        assertThat(result).isFalse();
    }


    @Test
    void reissueUserPasswordByEmail() {

    }

    @Test
    void certificationReissuePasswordConfirm() {
    }
}
