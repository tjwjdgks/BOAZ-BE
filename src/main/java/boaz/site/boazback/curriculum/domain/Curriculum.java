package boaz.site.boazback.curriculum.domain;

import boaz.site.boazback.common.converter.CurriculumTypeConverter;
import boaz.site.boazback.common.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "curriculum")
public class Curriculum extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long curriculumId;

    @Column(unique = true, nullable = false)
    private int trackId;

    private String title;

    @Convert(converter = CurriculumTypeConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> shortContentDescription;

    @Convert(converter = CurriculumTypeConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> contentDetail;

}
