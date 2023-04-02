package boaz.site.boazback.review.domain;


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
@Table(name = "review")
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String writer;

    private String year;

    @Column(columnDefinition = "TEXT")
    private String imageUrl;
    @Column(columnDefinition = "TEXT")
    private String track;
    @Column(columnDefinition = "TEXT")
    private String job;
    @Column(columnDefinition = "TEXT")
    private String company;
    @Column(columnDefinition = "TEXT")
    private String contents;

    private String createdUser;
    private String modifiedUser;

    @Builder
    public Review(Long id, String writer, String year, String imageUrl, String track, String job, String company, String contents, String createdUser, String modifiedUser) {
        this.id = id;
        this.writer = writer;
        this.year = year;
        this.imageUrl = imageUrl;
        this.track = track;
        this.job = job;
        this.company = company;
        this.contents = contents;
        this.createdUser = createdUser;
        this.modifiedUser = modifiedUser;
    }
}
