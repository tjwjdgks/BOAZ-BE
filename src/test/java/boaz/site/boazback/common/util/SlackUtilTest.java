package boaz.site.boazback.common.util;

import boaz.site.boazback.common.config.SlackConfig;
import boaz.site.boazback.question.domain.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SlackUtilTest {

    private SlackConfig slackConfig;
    private SlackUtil slackUtil;

    Question question;
    @BeforeEach
    void setUp() {
        String slackUrl = "<SLACK WEB HOOK CHANNEL >";
        String errorUrl = "<SLACK WEB HOOK  CHANNEL FOR ERROR>";
        slackConfig = SlackConfig.builder()
                .webhookUrl(slackUrl)
                .errorUrl(errorUrl)
                .build();
        slackConfig.setUp();
        slackUtil = new SlackUtil(slackConfig);
    }

    @Test
    @Disabled("Slack Api test 완료")
    void sendSlackMessage() {
        question = Question.builder()
                .detail("teest")
                .email("test@naver.com")
                .title("slack sdk testing")
                .name("test")
                .build();
        slackUtil.sendQuestionMessage(question);
    }

    @Test
    @Disabled("Error Message test 완료")
    void sendSlackErrorMessage() {
        try{
            int a = 1 / 0;
        }catch(Exception e){
            slackUtil.sendErrorMessage(e.getMessage());
        }
    }
}
