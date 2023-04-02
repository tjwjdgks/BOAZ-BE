package boaz.site.boazback.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
public class RedirectionConfig {
    @Value("${url.redirection}")
    private String redirectionUrl;

    public RedirectionConfig(String redirectionUrl) {
        this.redirectionUrl = redirectionUrl;
    }
}
