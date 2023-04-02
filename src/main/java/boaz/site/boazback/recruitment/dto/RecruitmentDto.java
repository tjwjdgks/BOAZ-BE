package boaz.site.boazback.recruitment.dto;

import boaz.site.boazback.recruitment.domain.Recruitment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class RecruitmentDto {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class RecruitmentResponse {
        private String notificationUrl;
        private String recruitmentUrl;
        private String faqUrl;
        @JsonProperty("isRecruitmentDuration")
        private boolean isRecruitmentDuration;

        @Builder(builderMethodName = "translateEntityBuilder",buildMethodName = "translate")
        public RecruitmentResponse(Recruitment recruitment) {
            this.isRecruitmentDuration = recruitment.isRecruitmentDuration();
            this.faqUrl = recruitment.getFaqUrl();
            this.recruitmentUrl = recruitment.getRecruitmentUrl();
            this.notificationUrl = recruitment.getNotificationUrl();
        }
    }
}
