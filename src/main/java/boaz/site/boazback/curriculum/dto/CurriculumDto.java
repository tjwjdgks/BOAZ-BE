package boaz.site.boazback.curriculum.dto;

import boaz.site.boazback.curriculum.domain.Curriculum;
import boaz.site.boazback.curriculum.domain.Skill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CurriculumDto {

    private int trackId;

    private String title;

    private List<String> shortContentDescription;

    private List<String> contentDetail;

    public CurriculumDto(Curriculum curriculum) {
        this.trackId = curriculum.getTrackId();
        this.title = curriculum.getTitle();
        this.shortContentDescription = curriculum.getShortContentDescription();
        this.contentDetail = curriculum.getContentDetail();
    }
}
