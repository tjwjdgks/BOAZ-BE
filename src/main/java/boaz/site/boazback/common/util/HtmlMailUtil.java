package boaz.site.boazback.common.util;

import boaz.site.boazback.common.domain.TracingFunction;
import boaz.site.boazback.common.exception.CertificationException;
import boaz.site.boazback.email.domain.EmailConfig;
import boaz.site.boazback.email.domain.EmailType;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.MimeMessage;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class HtmlMailUtil implements MailUtil {

    private final EmailConfig emailConfig;
    private final JavaMailSender sender;
    private final FreeMarkerConfigurer freeMarkerConfigurer;

    @Override
    public void sendMailForJoin(String receiverEmailAddress, String certificateCode) {
        MimeMessage message = sender.createMimeMessage();
        try {
            // set mediaType
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            Configuration configuration = freeMarkerConfigurer.createConfiguration();
            Template t = configuration.getTemplate(EmailType.JOIN.getTemplatePath());
            Map<String, Object> model = new HashMap<>();
            String encodedMail = encodeStringData(receiverEmailAddress, StandardCharsets.UTF_8);
            model.put("url", emailConfig.getUrl());
            model.put("code", certificateCode);
            model.put("mail", encodedMail);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
            helper.setSubject(EmailType.JOIN.getTitle());
            helper.setTo(receiverEmailAddress);
            helper.setText(html, true);
            helper.setFrom(emailConfig.getSenderEmail());
            sender.send(message);
        } catch (Exception e) {
            throw CertificationException.CERTIFICATION_EMAIL_ERROR;
        }
    }

    @Override
    public void sendEmailForPasswordReissue(String receiverEmailAddress, String certificateCode) {
        MimeMessage message = sender.createMimeMessage();
        try {
            // set mediaType
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            Template t = freeMarkerConfigurer.createConfiguration().getTemplate(EmailType.REISSUE.getTemplatePath());
            Map<String, Object> model = new HashMap<>();
            model.put("code", certificateCode);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
            helper.setSubject(EmailType.REISSUE.getTitle());
            helper.setTo(receiverEmailAddress);
            helper.setText(html, true);
            helper.setFrom(emailConfig.getSenderEmail());
            sender.send(message);
        } catch (Exception e) {
            throw CertificationException.CERTIFICATION_EMAIL_ERROR;
        }
    }
    @TracingFunction
    private String encodeStringData(String data, Charset charset) {
        byte[] getBytesFromString = data.getBytes(charset);
        BigInteger bigInteger = new BigInteger(1, getBytesFromString);
        String convertedResult = String.format("%x", bigInteger);
        return convertedResult;
    }

}
