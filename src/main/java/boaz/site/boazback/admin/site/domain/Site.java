package boaz.site.boazback.admin.site.domain;

import boaz.site.boazback.admin.site.dto.SiteDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@ToString
@Table(name = "site")
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String surveyUrl;

    private String authorityCode;

    @Builder
    public Site(Long id, String surveyUrl, String authorityCode) {
        this.id = id;
        this.surveyUrl = surveyUrl;
        this.authorityCode = authorityCode;
    }

    public Site(SiteDto.Request request) {
        this.authorityCode = request.getAuthorityCode();
        this.surveyUrl = request.getGoogleFormUrl();
    }


}
