package boaz.site.boazback.email.domain;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class EmailConfig {

    @Value("${spring.mail.username}")
    private String SenderEmail;
    @Value("${spring.mail.password}")
    private String SenderPassword;
    @Value("${certification.url}")
    private String url;

    public EmailConfig() {
    }

    public EmailConfig(String senderEmail, String senderPassword, String url) {
        this.SenderEmail = senderEmail;
        this.SenderPassword = senderPassword;
        this.url = url;
    }

}
