package boaz.site.boazback.comment.domain;

import boaz.site.boazback.comment.dto.SubCommentDto;
import boaz.site.boazback.common.domain.BaseTimeEntity;

import boaz.site.boazback.user.domain.User;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "subcomment")
@ToString(of = {"id","subCommentText","editName"})
public class SubComment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // comment 리스트로 불러오기
    private String editName;
    @Column(columnDefinition = "LONGTEXT")
    private String subCommentText;
    private boolean erase;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public SubCommentDto toSubCommentDto(){
        SubCommentDto sub = SubCommentDto.builder()
                .subCommentId(this.id)
                .subCommentEditName(this.editName)
                .createdDate(this.getCreatedDate())
                .modifiedDate(this.getModifiedDate())
                .erase(this.erase)
                .editName(this.editName)
                .editId(this.user.getId().toString())
                .commentId(this.comment.getId())
                .subCommentText(isErase()? "": this.subCommentText)
                .build();
        return sub;
    }
    public void addComment(Comment comment){
        this.comment = comment;
        comment.getSubComments().add(this);
    }

    public SubComment updateContent(String content) {
        this.subCommentText = content;
        return this;
    }

    public void eraseContent() {
        this.erase = true;
    }
}
