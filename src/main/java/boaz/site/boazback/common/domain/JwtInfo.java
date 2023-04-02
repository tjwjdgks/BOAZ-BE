package boaz.site.boazback.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtInfo {

    private UUID id;
    private int section;
    private String name;
    private String email;
    private String year;
    private String editName;
    private int admin;

}
