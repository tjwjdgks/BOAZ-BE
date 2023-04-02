package boaz.site.boazback.common.util;

public interface MailUtil {
    void sendMailForJoin(String receiverEmailAddress, String certificateCode);
    void sendEmailForPasswordReissue(String receiverEmailAddress, String certificateCode);
}
