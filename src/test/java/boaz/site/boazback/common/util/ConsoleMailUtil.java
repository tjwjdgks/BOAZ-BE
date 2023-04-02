package boaz.site.boazback.common.util;


public class ConsoleMailUtil implements MailUtil {
    @Override
    public void sendMailForJoin(String receiverEmailAddress, String certificateCode) {
        System.out.println("send mail for join!!!");
    }

    @Override
    public void sendEmailForPasswordReissue(String receiverEmailAddress, String certificateCode) {
        System.out.println("send email for password reissue!!");
    }
}
