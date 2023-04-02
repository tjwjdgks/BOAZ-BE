package boaz.site.boazback.intro.domain;


import boaz.site.boazback.common.domain.BaseTimeEntity;
import boaz.site.boazback.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class IntroSubComment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "LONGTEXT")
    private String contents;

    @ManyToOne
    @JoinColumn(name ="intro_id")
    private Intro intro;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ColumnDefault(value = "false")
    private boolean erase;

    @Builder
    public IntroSubComment(Long id, String contents, Intro intro, User user,boolean erase) {
        this.id = id;
        this.contents = contents;
        this.intro = intro;
        this.user = user;
        this.erase = erase;
    }

    public IntroSubComment updateSubComment(String contents){
        return new IntroSubComment(this.id, contents, this.intro, this.user,this.erase);
    }

    public IntroSubComment eraseSubComment(){
        return new IntroSubComment(this.id, this.contents, this.intro, this.user,true);
    }
}
