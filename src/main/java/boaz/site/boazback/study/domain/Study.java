package boaz.site.boazback.study.domain;

import javax.persistence.*;

import boaz.site.boazback.comment.domain.Comment;
import boaz.site.boazback.common.converter.StringArrayConverter;
import boaz.site.boazback.common.domain.BaseTimeEntity;
import boaz.site.boazback.user.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"id","title","contents","imgUrl"})
@Entity
@Table(name = "study")
public class Study extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String title;
    @Column(columnDefinition = "LONGTEXT")
    private String contents;

    @Convert(converter = StringArrayConverter.class)
    @Builder.Default
    @Column(columnDefinition = "LONGTEXT")
    private List<String> imgUrl = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "memberId")
    private User editor;

    @ManyToOne
    @JoinColumn(name = "studyCategoryId")
//    @JsonIgnore
    private StudyCategory studyCategory;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "study")
    @JsonIgnore
    private List<Comment> comments;


    @ColumnDefault("0")
    @Builder.Default
    private Long hits = 0L;

    private boolean published = false;


    public Study updateStudy(String title, String contents,List<String> imgUrl){
        this.title = title;
        this.contents = contents;
        this.imgUrl = imgUrl;
        this.published = true;
        return this;
    }

    public void updateCategory(StudyCategory studyCategory) {
        this.studyCategory = studyCategory;
    }
}
