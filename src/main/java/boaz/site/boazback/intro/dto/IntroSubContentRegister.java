package boaz.site.boazback.intro.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class IntroSubContentRegister {
    @NotBlank
    private String content;
    @NotNull
    @Min(0)
    private Long commentId;

    @Builder
    public IntroSubContentRegister(String content, Long commentId) {
        this.content = content;
        this.commentId = commentId;
    }
}
