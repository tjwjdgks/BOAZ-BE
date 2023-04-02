package boaz.site.boazback.intro.domain;


import boaz.site.boazback.common.domain.BaseTimeEntity;
import boaz.site.boazback.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "intro")
@ToString
@Where(clause = "erase=false")
public class Intro extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "LONGTEXT")
    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "intro_id")
    private List<IntroSubComment> subComments;

    @ColumnDefault(value = "false")
    private boolean erase;

    @Builder
    public Intro(Long id, String contents, User user,List<IntroSubComment>introSubComments, boolean erase) {
        this.id = id;
        this.contents = contents;
        this.user = user;
        this.subComments = introSubComments;
        this.erase = erase;
    }

    public Intro updateContent(String contents){
        this.contents = contents;
        return this;
    }
    public Intro eraseIntro(){
        this.erase = true;
        return this;
    }
}
