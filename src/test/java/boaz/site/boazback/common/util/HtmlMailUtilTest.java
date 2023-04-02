package boaz.site.boazback.common.util;

import boaz.site.boazback.common.config.FreeMarkerConfig;
import boaz.site.boazback.email.domain.EmailConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

class HtmlMailUtilTest {

    private JavaMailSender javaMailSender;
    private EmailConfig eMailConfig;
    private MailUtil mailUtil;

    @BeforeEach
    void setUp() {
        EmailConfig eMailConfig = new EmailConfig("test@gmail.com","test","http://localhost:8080");
        FreeMarkerConfig freeMarkerConfig = new FreeMarkerConfig();
        JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
        javaMailSenderImpl.setPort(587);
        javaMailSenderImpl.setHost("smtp.gmail.com");
        Properties javaProperties = new Properties();

        javaProperties.setProperty("mail.smtp.auth", "true");
        javaProperties.setProperty("mail.smtp.starttls.required", "true");
        javaProperties.setProperty("mail.smtp.starttls.enable", "true");
        javaProperties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        javaProperties.setProperty("mail.smtp.socketFactory.fallback", "false");
        javaProperties.setProperty("mail.smtp.port", "465");
        javaProperties.setProperty("mail.smtp.socketFactory.port", "465");
        javaMailSenderImpl.setJavaMailProperties(javaProperties);
        javaMailSenderImpl.setUsername(eMailConfig.getSenderEmail());
        javaMailSenderImpl.setPassword(eMailConfig.getSenderPassword());
        try{
            mailUtil = new HtmlMailUtil(eMailConfig, javaMailSenderImpl, freeMarkerConfig.freemarkerConfig());
        }catch(Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }


    @Test
    @Disabled("이메일 테스트 완료")
    void sendMailForJoin() {
        mailUtil.sendMailForJoin("test@naver.com","test");
    }

    @Test
    @Disabled("이메일 테스트 완료")
    void sendEmailForPasswordReissue() {
        mailUtil.sendEmailForPasswordReissue("test@naver.com","test");
    }
}
