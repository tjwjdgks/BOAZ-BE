package boaz.site.boazback.curriculum.domain;

import boaz.site.boazback.common.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@ToString
@Table(name = "member_suggestion")
public class MemberSuggestion extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String writer;

    private String year;

    @Column(columnDefinition = "TEXT")
    private String track;

    @Column(columnDefinition = "TEXT")
    private String emojiUrl;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String createdUser;

    private String modifiedUser;

    private int trackId;

    @Builder
    public MemberSuggestion(Long id, String writer, String year, String track, String emojiUrl, String content, String createdUser, String modifiedUser,int trackId) {
        this.id = id;
        this.writer = writer;
        this.year = year;
        this.track = track;
        this.emojiUrl = emojiUrl;
        this.content = content;
        this.createdUser = createdUser;
        this.modifiedUser = modifiedUser;
        this.trackId = trackId;
    }
}
