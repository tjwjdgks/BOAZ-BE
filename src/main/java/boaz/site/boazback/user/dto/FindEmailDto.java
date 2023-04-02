package boaz.site.boazback.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class FindEmailDto {

    @NotEmpty
    private String userName;

    @Email
    @NotEmpty
    private String userEmail;

    @NotEmpty
    private String year; //기수

    @NotEmpty
    private String birthDate;

    @Min(value = 1, message = "not found track")
    @Max(value = 3, message = "not found track")
    private int section;
}
