package boaz.site.boazback.user.dto;


import boaz.site.boazback.user.domain.User;
import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserDto {

    @Min(value = 1 ,message = "not found track")
    @Max(value = 3 ,message = "not found track")
    private int section;
    @NotEmpty
    private String name;
    @Email @NotEmpty
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty
    private String year; //기수
    @NotEmpty
    private String authenticationCode;

    private String birthDate;

    @Builder
    public UserDto(int section, String name, String email, String password, String year, String authenticationCode, String birthDate) {
        this.section = section;
        this.name = name;
        this.email = email;
        this.password = password;
        this.year = year;
        this.authenticationCode = authenticationCode;
        this.birthDate = birthDate;
    }


    public User transForm() {
        return User.builder()
                .birthDate(this.birthDate)
                .email(this.email)
                .name(this.name)
                .password(this.password)
                .section(this.section)
                .year(this.year)
                .build();
    }
}
