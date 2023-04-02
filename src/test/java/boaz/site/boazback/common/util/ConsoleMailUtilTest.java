package boaz.site.boazback.common.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

public class ConsoleMailUtilTest {

    private MailUtil mailUtil = new ConsoleMailUtil();

    @Test
    void testEmailScope(){
        mailUtil.sendMailForJoin("test","test");
    }
}
