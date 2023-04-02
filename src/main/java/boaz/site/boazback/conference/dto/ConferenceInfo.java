package boaz.site.boazback.conference.dto;

import boaz.site.boazback.conference.domain.Conference;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
public class ConferenceInfo {
    private Long id;
    private String title;
    private String movieUrl;
    private String detail;
    private String participants;
    private UUID editor;
    private String editorName;
    private List<String> imageUrls;
    private Long hits;
    private String slideShareUrl;

    private String fileUrl;

    public ConferenceInfo() {
    }

    @Builder
    public ConferenceInfo(Long id, String title, String movieUrl, String detail, String participants, UUID editor, String editorName, List<String> imageUrls, Long hits, String slideShareUrl, String fileUrl) {
        this.id = id;
        this.title = title;
        this.movieUrl = movieUrl;
        this.detail = detail;
        this.participants = participants;
        this.editor = editor;
        this.editorName = editorName;
        this.imageUrls = imageUrls;
        this.hits = hits;
        this.slideShareUrl = slideShareUrl;
        this.fileUrl = fileUrl;
    }

    public ConferenceInfo(Conference conference) {
        this.id = conference.getId();
        this.title = conference.getTitle();
        this.movieUrl = conference.getMovieUrl();
        this.detail = conference.getDetail();
        this.participants = conference.getParticipants();
        this.editor = conference.getEditor().getId();
        this.editorName = conference.getEditor().getEditName();
        this.imageUrls = conference.getImageUrls();
        this.hits = conference.getHits();
        this.slideShareUrl = conference.getSlideShareUrl();
        this.fileUrl = conference.getFileUrl();
    }

}
