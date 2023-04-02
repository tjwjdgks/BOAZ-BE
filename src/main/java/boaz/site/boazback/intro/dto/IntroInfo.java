package boaz.site.boazback.intro.dto;

import boaz.site.boazback.intro.domain.Intro;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
public class IntroInfo {
    private Long id;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String editName;
    private UUID editorId;
    private List<IntroSubCommentInfo> subComments;
    private boolean erase;

    public IntroInfo(Intro intro) {
        this.id = intro.getId();
        this.content =  intro.isErase() ? "" : intro.getContents();
        this.createdDate = intro.getCreatedDate();
        this.modifiedDate = intro.getModifiedDate();
        this.editName = intro.getUser().getEditName();
        this.editorId = intro.getUser().getId();
        this.subComments = new ArrayList<>();
        this.erase = intro.isErase();
    }

    public IntroInfo() {
    }
}

