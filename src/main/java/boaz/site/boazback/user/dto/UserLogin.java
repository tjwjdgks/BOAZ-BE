package boaz.site.boazback.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
public class UserLogin {

    @Email
    @NotBlank
    private String email;
    @NotEmpty
    private String password;

    public UserLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
