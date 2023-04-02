package boaz.site.boazback.comment.dto;

import boaz.site.boazback.common.domain.JwtInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
public class CommentDto {
    private Long commentId;
    private Long studyId;
    private String editId;
    private String editName;
    private String commentText;
    private boolean erase;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private List<SubCommentDto> subCommentDtoList;
}
