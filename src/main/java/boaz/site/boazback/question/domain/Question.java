package boaz.site.boazback.question.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@Entity
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String email;
    @Column(columnDefinition = "TEXT")
    private String title;
    @Column(columnDefinition = "LONGTEXT")
    private String detail;

@Builder
    public Question(String name, String email, String title, String detail) {
        this.name = name;
        this.email = email;
        this.title = title;
        this.detail = detail;
    }
}
