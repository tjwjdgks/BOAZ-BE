package boaz.site.boazback.common.config;

import com.slack.api.Slack;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Getter
@Component
@NoArgsConstructor
public class SlackConfig {

    @Value("${slack.url}")
    private String webhookUrl;

    @Value("${slack.error}")
    private String errorUrl;

    private Slack slack;
    @Builder
    public SlackConfig(String webhookUrl, String errorUrl, Slack slack) {
        this.webhookUrl = webhookUrl;
        this.errorUrl = errorUrl;
        this.slack = slack;
    }

    @PostConstruct
    public void setUp(){
        slack = Slack.getInstance();
    }
}
