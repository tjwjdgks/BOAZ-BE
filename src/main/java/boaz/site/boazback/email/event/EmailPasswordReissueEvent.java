package boaz.site.boazback.email.event;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
public class EmailPasswordReissueEvent {
    private String email;
    private String certificationCode;
    public EmailPasswordReissueEvent(@Email @NotEmpty String email, String certificationCode) {
        this.email = email;
        this.certificationCode = certificationCode;
    }
}
