package boaz.site.boazback.common.util;

import boaz.site.boazback.common.config.SlackConfig;
import boaz.site.boazback.common.domain.TracingFunction;
import boaz.site.boazback.question.domain.Question;
import com.slack.api.model.Attachment;
import com.slack.api.webhook.Payload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SlackUtil {
    private final SlackConfig slackConfig;
    private static final String NEWLINE = System.lineSeparator();
    private static final String SLACK_USER_NAME = "BOAZ SERVER";

    /**
     * question내용을 slack webhook을 활용해 slack 채널에 메시지 보내기
     * @param question 문의 온 내용
     * @see SlackUtil#createAttachmentList(StringBuilder, String, String)
     * @see SlackUtil#createPayload(List)
     * @see SlackUtil#sendSlackMessage(Payload)
     */
    @TracingFunction
    public void sendQuestionMessage(Question question){
        StringBuilder sb = new StringBuilder();
        sb.append("[TITLE] : ").append(question.getTitle()).append(NEWLINE)
                .append("[E-MAIL] : ").append(question.getEmail()).append(NEWLINE)
                .append("[NAME] : ").append(question.getName()).append(NEWLINE)
                .append("[DETAIL] : ").append(question.getDetail()).append(NEWLINE);
        List<Attachment> attachmentList = createAttachmentList(sb,"#00dc00","[Notification] : BOAZ 문의");
        Payload payload = createPayload(attachmentList);
        sendSlackMessage(payload);
    }

    public void sendErrorMessage(String errorMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append("[TITLE] : ").append("Error").append(NEWLINE)
                .append("[DETAIL] : ").append(errorMessage).append(NEWLINE);
        List<Attachment> attachmentList = createAttachmentList(sb,"#00dc00","[Server Error] : Error 발생");
        Payload payload = createPayload(attachmentList);
        sendSlackError(payload);
    }

    private List<Attachment> createAttachmentList(StringBuilder text, String color, String preText) {
        Attachment attachment = Attachment.builder()
                .text(String.valueOf(text))
                .color(color)
                .pretext(preText)
                .build();
        List<Attachment> attachmentList = new ArrayList<>();
        attachmentList.add(attachment);
        return attachmentList;
    }


    private Payload createPayload(List<Attachment> attachmentList) {
        return Payload.builder()
                .username(SLACK_USER_NAME)
                .attachments(attachmentList)
                .build();
    }

    /**
     *  webhook url을 활용하여 slack message 보내기
     * @param payload
     */
    @TracingFunction
    private void sendSlackMessage(Payload payload){
        try{
            slackConfig.getSlack().send(slackConfig.getWebhookUrl(), payload);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @TracingFunction
    private void sendSlackError(Payload payload){
        try{
            slackConfig.getSlack().send(slackConfig.getErrorUrl(), payload);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

}
