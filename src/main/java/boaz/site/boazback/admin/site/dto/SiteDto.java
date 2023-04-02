package boaz.site.boazback.admin.site.dto;


import boaz.site.boazback.admin.site.domain.Site;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class SiteDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Request{
        private String googleFormUrl;
        private String authorityCode;

        @Builder
        public Request(String googleFormUrl, String authorityCode) {
            this.googleFormUrl = googleFormUrl;
            this.authorityCode = authorityCode;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response{
        private String googleFormUrl;
        private String authorityCode;
        @Builder
        public Response(String googleFormUrl, String authorityCode) {
            this.googleFormUrl = googleFormUrl;
            this.authorityCode = authorityCode;
        }

        public Response(Site site) {
            this.googleFormUrl = site.getSurveyUrl();
            this.authorityCode = site.getAuthorityCode();
        }

    }

}
