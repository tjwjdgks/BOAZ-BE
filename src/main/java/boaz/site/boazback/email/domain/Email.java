package boaz.site.boazback.email.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Email  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authCode;

    private String userEmail;

    @Enumerated(EnumType.STRING)
    private EmailType emailType;


    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime  modifiedDate;

    @Builder
    public Email(Long id, String authCode, String userEmail, EmailType emailType, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.authCode = authCode;
        this.userEmail = userEmail;
        this.emailType = emailType;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public Email updateCertification(String certificationCode) {
        this.authCode = certificationCode;
        this.modifiedDate = LocalDateTime.now();
        return this;
    }
}
