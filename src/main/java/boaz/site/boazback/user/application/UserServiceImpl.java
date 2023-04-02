package boaz.site.boazback.user.application;

import boaz.site.boazback.common.domain.TracingFunction;
import boaz.site.boazback.common.exception.CertificationException;
import boaz.site.boazback.common.exception.UserException;
import boaz.site.boazback.common.util.CertificationUtil;
import boaz.site.boazback.common.util.MailUtil;
import boaz.site.boazback.email.application.EmailRepository;
import boaz.site.boazback.email.domain.Email;
import boaz.site.boazback.email.domain.EmailType;
import boaz.site.boazback.email.event.EmailJoinEvent;
import boaz.site.boazback.email.event.EmailPasswordReissueEvent;
import boaz.site.boazback.user.dto.FindEmailDto;
import boaz.site.boazback.user.domain.User;
import boaz.site.boazback.user.dto.UserDto;
import boaz.site.boazback.user.dto.UserLogin;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.MDC;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Logger logger = LogManager.getLogger();
    private final UserRepository userRepository;
    private final EmailRepository emailRepository;
    private final CertificationUtil certificationUtil;
    private final MailUtil mailUtil;
    private final ApplicationEventPublisher eventPublisher;
    @Override
    @Transactional
    @TracingFunction
    public User registerUser(UserDto userDto) {
        logger.info("register user service start");
        String hashpw = BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt());
        userDto.setPassword(hashpw);
        User user = userDto.transForm();
        MDC.getCopyOfContextMap();
        Optional<User> registerUser = userRepository.findByEmail(userDto.getEmail());

        if (registerUser.isPresent()) {
            //추후 Custom Exception으로 처리 예정
            logger.error("user already registered");
            throw UserException.USER_ALREADY_REGISTERED;
        }
        if (!isAuthenticated(userDto.getAuthenticationCode())) {
            logger.error("user certificartion v2 fail ");
            throw CertificationException.CERTIFICATION_NOT_SAME;
        }
        User savedUser = userRepository.save(user);
        //인증코드 생성하기
        String certificationCode = certificationUtil.createCertificationCode();

        //이메일 보내기
        //mailUtil.sendMailForJoin(userDto.getEmail(), certificationCode);
        eventPublisher.publishEvent(new EmailJoinEvent(userDto.getEmail(),certificationCode));

        Email email = null;
        // 메일 테이블 중복체크 로직
        Optional<Email> checkEmail = emailRepository.findByUserEmailAndEmailType(userDto.getEmail(), EmailType.JOIN);
        if (checkEmail.isPresent()) {
            email = checkEmail.get().updateCertification(certificationCode);
        } else {
            email = Email.builder()
                    .emailType(EmailType.JOIN)
                    .authCode(certificationCode)
                    .userEmail(userDto.getEmail())
                    .createdDate(LocalDateTime.now())
                    .modifiedDate(LocalDateTime.now())
                    .build();
        }
        emailRepository.save(email);
        logger.info("register user service end");
        return savedUser;
    }

    @Override
    @Transactional
    @TracingFunction
    public void deleteUser(UUID uid) {
        logger.info("delete user service start");
        userRepository.deleteById(uid);
        logger.info("delete user service end");
    }

    @Override
    @TracingFunction
    public User loginUser(UserLogin userLogin) {

        Optional<User> findUser = userRepository.findByEmail(userLogin.getEmail());
        if (findUser.isEmpty()) {
            logger.error("user not exist");
            throw UserException.LOGIN_ERROR;
        }

        if (!BCrypt.checkpw(userLogin.getPassword(), findUser.get().getPassword())) {
            logger.error("password not correct");
            throw UserException.PASSWORD_ERROR;
        }

        // login 시 인증했는지에 대해 체크하기
        return findUser.get();
    }

    @Override
    public Optional<User> findUser(UUID uid) {
        return userRepository.findById(uid);
    }

    @Override
    public boolean isExistUser(UUID uid) {
        Optional<User> user = findUser(uid);
        return user.isPresent();
    }

    @Override
    @Transactional
    public void certificationSignupConfirm(String certificationCode, String encodedEmail) {
        logger.info("emailCheckUser start");

        String userEmail = checkCertificationJoinCode(certificationCode, encodedEmail);

        Optional<User> findUser = userRepository.findByEmail(userEmail);
        if (findUser.isEmpty()) {
            throw UserException.USER_NOT_FOUND;
        }

        if (findUser.get().isEmailCheck()) {
            throw UserException.USER_ALREADY_CERTIFIED;
        }

        User certificatedUser = findUser.get().certificateEmail();
        userRepository.save(certificatedUser);
        logger.info("emailCheckUser end");
    }

    @Override
    @Transactional
    public void reissueUserPasswordByEmail(FindEmailDto findEmailDto) {
        logger.info("reissueUserPasswordByEmail  Service Start");
        // 정보 체크하기
        //code 생성
        String certificationCode = certificationUtil.createCertificationCode();
        // 이메일 보내기
        //mailUtil.sendEmailForPasswordReissue(findEmailDto.getUserEmail(), certificationCode);
        eventPublisher.publishEvent(new EmailPasswordReissueEvent(findEmailDto.getUserEmail(),certificationCode));

        // 메일 테이블 중복체크 로직
        Optional<Email> checkEmail = emailRepository.findByUserEmailAndEmailType(findEmailDto.getUserEmail(), EmailType.REISSUE);
        Email email = null;
        if (checkEmail.isPresent()) {
            email = checkEmail.get().updateCertification(certificationCode);
        } else {
            email = Email.builder()
                    .emailType(EmailType.REISSUE)
                    .authCode(certificationCode)
                    .userEmail(findEmailDto.getUserEmail())
                    .createdDate(LocalDateTime.now())
                    .modifiedDate(LocalDateTime.now())
                    .build();
        }

        //이메일 테이블에 인증 등록하기
        emailRepository.save(email);
        logger.info("reissueUserPasswordByEmail  Service End");
    }

    @Override
    @Transactional
    public void certificationReissuePasswordConfirm(String certificationCode, String newPassword) {
        logger.info("certificationSignupConfirm Service start");
        String userEmail = checkCertificationResetCode(certificationCode);
        Optional<User> findUser = userRepository.findByEmail(userEmail);
        if (findUser.isEmpty()) {
            throw UserException.USER_NOT_FOUND;
        }
        String newHashpw = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        findUser.get().changePassword(newHashpw);
        userRepository.save(findUser.get());
        logger.info("certificationSignupConfirm Service end");
    }

    @Override
    @Transactional
    public void resendJoinConfirmMail(String email) {
        Optional<Email> checkEmail = emailRepository.findByUserEmailAndEmailType(email, EmailType.JOIN);
        String newCertificationCode = certificationUtil.createCertificationCode();
        //mailUtil.sendMailForJoin(email, newCertificationCode);
        eventPublisher.publishEvent(new EmailJoinEvent(email,newCertificationCode));
        Email newEmail;
        if (checkEmail.isEmpty()) {
            newEmail = Email.builder()
                    .emailType(EmailType.JOIN)
                    .authCode(newCertificationCode)
                    .userEmail(email)
                    .createdDate(LocalDateTime.now())
                    .modifiedDate(LocalDateTime.now())
                    .build();
        }else{
             newEmail = checkEmail.get().updateCertification(newCertificationCode);
        }
        emailRepository.save(newEmail);
    }


    private String checkCertificationJoinCode(String certificateCode, String email) {
        String decodedEmail = decodeStringData(email, StandardCharsets.UTF_8);
        Optional<Email> findEmail = emailRepository.findByUserEmailAndEmailType(decodedEmail, EmailType.JOIN);

        if (findEmail.isEmpty()) {
            throw CertificationException.CERTIFICATION_NOT_FOUND;
        }
        certificationUtil.checkExpiredCertificationCode(findEmail.get().getModifiedDate().plusHours(3));
        certificationUtil.checkCertificationCode(certificateCode, findEmail.get().getAuthCode());
        emailRepository.deleteById(findEmail.get().getId());
        return findEmail.get().getUserEmail();
    }

    // 패스워드 재발급 이메일 인증하기
    public String checkCertificationResetCode(String certificateCode) {
        Optional<Email> findEmail = emailRepository.findByAuthCodeAndEmailType(certificateCode, EmailType.REISSUE);
        if (findEmail.isEmpty()) {
            throw CertificationException.CERTIFICATION_NOT_FOUND;
        }
        certificationUtil.checkExpiredCertificationCode(findEmail.get().getModifiedDate().plusHours(3));
        certificationUtil.checkCertificationCode(certificateCode, findEmail.get().getAuthCode());
        emailRepository.deleteById(findEmail.get().getId());
        return findEmail.get().getUserEmail();
    }

    private boolean isAuthenticated(String code) {
        return code != null && code.equals("helloboaz");
    }

    private String decodeStringData(String encodedData, Charset charset) {
        BigInteger bigInteger = new BigInteger(encodedData, 16);
        byte[] encodedDataBytes = bigInteger.toByteArray();
        return new String(encodedDataBytes, charset);
    }

}
