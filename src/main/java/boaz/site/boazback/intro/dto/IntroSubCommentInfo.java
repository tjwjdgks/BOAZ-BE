package boaz.site.boazback.intro.dto;

import boaz.site.boazback.intro.domain.IntroSubComment;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
public class IntroSubCommentInfo {
    private Long id;
    private String contents;
    private String editName;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private UUID editorId;
    private boolean erase;

    public IntroSubCommentInfo(IntroSubComment introSubComment) {
        this.id = introSubComment.getId();
        this.contents = introSubComment.isErase() ?  "": introSubComment.getContents();
        this.editName = introSubComment.getUser().getEditName();
        this.createdDate = introSubComment.getCreatedDate();
        this.modifiedDate = introSubComment.getModifiedDate();
        this.erase = introSubComment.isErase();
        this.editorId = introSubComment.getUser().getId();
    }

    public IntroSubCommentInfo() {
    }
}
