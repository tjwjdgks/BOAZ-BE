package boaz.site.boazback.conference.domain;

import boaz.site.boazback.common.converter.StringArrayConverter;
import boaz.site.boazback.common.domain.BaseTimeEntity;
import boaz.site.boazback.conference.dto.ConferenceUpdateInfo;
import boaz.site.boazback.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "conference")
@FilterDef(name = "publishFilter",
        parameters = @ParamDef(name = "isPublished", type = "boolean"),
        defaultCondition = "pro = :published"
)
@Filter(name="publishFilter",condition = "published= :isPublished")
public class Conference extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String title;
    @Column(columnDefinition = "TEXT")
    private String movieUrl;
    @Column(columnDefinition = "TEXT")
    private String slideShareUrl;
    @Column(columnDefinition = "LONGTEXT")
    private String detail;
    @Column(columnDefinition = "TEXT")
    private String participants;

    @OneToOne
    @JoinColumn(name = "userId")
    private User editor;

    @Convert(converter = StringArrayConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> imageUrls;

    @ColumnDefault("0")
    private Long hits;

    private boolean published;
    @Column(columnDefinition = "TEXT")
    private String fileUrl;


    public void setHits(Long hits) {
        this.hits = hits;
    }

    @Builder
    public Conference(Long id, String title, String movieUrl, String slideShareUrl, String detail, String participants, User editor, List<String> imageUrls, Long hits, boolean published, String fileUrl) {
        this.id = id;
        this.title = title;
        this.movieUrl = movieUrl;
        this.slideShareUrl = slideShareUrl;
        this.detail = detail;
        this.participants = participants;
        this.editor = editor;
        this.imageUrls = imageUrls;
        this.hits = hits;
        this.published = published;
        this.fileUrl = fileUrl;
    }




    public Conference publishConference(ConferenceUpdateInfo conferenceUpdateInfo){
        this.title = conferenceUpdateInfo.getTitle();
        this.movieUrl = conferenceUpdateInfo.getMovieUrl();
        this.slideShareUrl = conferenceUpdateInfo.getSlideShareUrl();
        this.detail = conferenceUpdateInfo.getDetails();
        this.participants = conferenceUpdateInfo.getParticipants();
        this.imageUrls = conferenceUpdateInfo.getImgUrls();
        this.fileUrl = conferenceUpdateInfo.getFileUrl();
        this.published = true;
        return this;
    }

@Builder(builderMethodName = "initBuilder",buildMethodName = "init")
    public Conference(User editor,  boolean published) {
        this.title = null;
        this.movieUrl = null;
        this.slideShareUrl = null;
        this.detail = null;
        this.participants = null;
        this.editor = editor;
        this.imageUrls = new ArrayList<String>();
        this.hits = 0L;
        this.fileUrl = null;
        this.published = published;
    }

}

