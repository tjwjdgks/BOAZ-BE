package boaz.site.boazback.study.dto;

import boaz.site.boazback.study.domain.Study;
import boaz.site.boazback.study.domain.StudyCategory;
import boaz.site.boazback.user.domain.User;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class StudyDto {

    @NotNull
    private Long studyCategoryId;
    @NotNull @NotEmpty
    private String title;
    @NotNull
    private String contents;
    @NotNull
    private UUID editor;
    private int isFile;

    private List<String> imgUrl = new ArrayList<>();

    @Builder
    public StudyDto(Long studyCategoryId, String title, String contents, UUID editor,
                    int isFile, List<String> imgUrl) {
        this.studyCategoryId = studyCategoryId;
        this.title = title;
        this.contents = contents;
        this.editor = editor;
        this.isFile = isFile;
        this.imgUrl = imgUrl;
    }

    public Study transForm(User user, StudyCategory studyCategory) {
        return Study.builder()
                .studyCategory(studyCategory)
                .editor(user)
                .contents(this.contents)
                .title(this.title)
                .imgUrl(this.imgUrl)
                .hits(0L)
                .published(true)
                .build();
    }
}
