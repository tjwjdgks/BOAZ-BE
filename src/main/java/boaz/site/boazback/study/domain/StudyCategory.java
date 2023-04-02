package boaz.site.boazback.study.domain;

import boaz.site.boazback.common.domain.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "studyCategory")
public class StudyCategory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    // subcomment list
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "studyCategory")
    @JsonIgnore
    private List<Study> studyList;

    @Builder
    public StudyCategory(Long id,String title) {
        this.id = id;
        this.title = title;
    }
}

