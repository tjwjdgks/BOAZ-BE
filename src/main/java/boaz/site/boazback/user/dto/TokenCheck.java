package boaz.site.boazback.user.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class TokenCheck {
    private Date expiredTime;
    private boolean expired;

    public TokenCheck(Date expiredTime, boolean expired) {
        this.expiredTime = expiredTime;
        this.expired = expired;
    }
}
