package boaz.site.boazback.recruitment.domain;

import boaz.site.boazback.common.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "recruitment")
public class Recruitment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String recruitmentUrl;

    @Column(columnDefinition = "TEXT")
    private String notificationUrl;

    @Column(columnDefinition = "TEXT")
    private String faqUrl;

    private boolean isRecruitmentDuration;

    private String createdUser;

    private String modifiedUser;
}
