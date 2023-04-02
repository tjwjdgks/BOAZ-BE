package boaz.site.boazback.comment.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
public class SubCommentDto {
    private Long subCommentId;
    private Long commentId;
    private String subCommentEditName;
    private String subCommentText;
    private String editName;
    private String editId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private boolean erase;

}
