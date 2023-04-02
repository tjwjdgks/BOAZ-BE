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
@Table(name = "skill")
public class Skill extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int trackId;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String logoUrl;

    @Builder
    public Skill(int trackId, String name, String logoUrl) {
        this.trackId = trackId;
        this.name = name;
        this.logoUrl = logoUrl;
    }
}
