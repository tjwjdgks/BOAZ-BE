package boaz.site.boazback.common.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ResendEmailToken {
    String email;

//    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
//    LocalDateTime createdDateTime;

    @Builder
    public ResendEmailToken(String email, LocalDateTime createdDateTime) {
        this.email = email;
//        this.createdDateTime = createdDateTime;
    }
}
