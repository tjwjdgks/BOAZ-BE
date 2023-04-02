package boaz.site.boazback.intro.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class IntroContentRegister {

    @NotBlank
    private String content;

    public IntroContentRegister(String content) {
        this.content = content;
    }
}
