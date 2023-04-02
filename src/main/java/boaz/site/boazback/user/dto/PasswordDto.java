package boaz.site.boazback.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class PasswordDto {

    @NotBlank
    private String newPassword;

    public PasswordDto(String newPassword) {
        this.newPassword = newPassword;
    }
}
