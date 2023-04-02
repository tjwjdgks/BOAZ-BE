package boaz.site.boazback.email.event;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
public class EmailJoinEvent {
    private String email;
    private String certificationCode;
    public EmailJoinEvent(@Email @NotEmpty String email, String certificationCode) {
        this.email = email;
        this.certificationCode = certificationCode;
    }
}
