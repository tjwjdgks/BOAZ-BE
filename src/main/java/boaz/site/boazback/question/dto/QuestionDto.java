package boaz.site.boazback.question.dto;

import boaz.site.boazback.question.domain.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class QuestionDto {

    @NotBlank
    private String name;
    @Email @NotBlank
    private String email;
    @NotBlank
    private String title;

    @NotBlank
    private String detail;

    @Builder
    public QuestionDto(String name, String email, String title, String detail) {
        this.name = name;
        this.email = email;
        this.title = title;
        this.detail = detail;
    }

    public Question transForm() {
        return Question.builder()
                .detail(this.detail)
                .email(this.email)
                .name(this.name)
                .title(this.title)
                .build();
    }
}
