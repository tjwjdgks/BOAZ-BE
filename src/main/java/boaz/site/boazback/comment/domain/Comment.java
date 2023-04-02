package boaz.site.boazback.comment.domain;


import boaz.site.boazback.comment.dto.CommentDto;
import boaz.site.boazback.common.domain.BaseTimeEntity;
import boaz.site.boazback.study.domain.Study;
import boaz.site.boazback.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comment")
@ToString(of = {"id","commentText","studyId","editName"})
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "LONGTEXT")
    private String commentText;
    private String editName;
    private boolean erase;

    // subcomment list
    @OneToMany(mappedBy ="comment", cascade = CascadeType.ALL)
    private List<SubComment> subComments;

    @ManyToOne
    @JoinColumn(name = "study_id")
    private Study study;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Comment(User user,Study study, String editName, String commentText) {
        this.user = user;
        this.study = study;
        this.editName = editName;
        this.commentText = commentText;
    }

    public CommentDto toDto(){
        CommentDto commentDto = CommentDto.builder()
                .commentId(this.id)
                .commentText(isErase()? "":this.commentText)
                .erase(this.erase)
                .studyId(this.study.getId())
                .editName(this.editName)
                .editId(this.user.getId().toString())
                .createdDate(this.getCreatedDate())
                .modifiedDate(this.getModifiedDate())
                .subCommentDtoList(subComments.stream()
                        .map(SubComment::toSubCommentDto)
                        .collect(Collectors.toList()))
                .build();
        return commentDto;
    }

    public Comment updateContent(String content) {
        this.commentText = content;
        return this;
    }

    public void eraseContent() {
        this.erase = true;
    }
}
