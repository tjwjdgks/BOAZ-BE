package boaz.site.boazback.conference.dto;

import boaz.site.boazback.conference.domain.Conference;
import boaz.site.boazback.user.domain.User;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
public class ConferenceDto {
    private String title;
    private String movieUrl;
    private String details;
    private String participants;
    private UUID editorId;
    private String slideShareUrl;

    @Builder
    public ConferenceDto(String title, String movieUrl, String details, String participants, UUID editorId, String slideShareUrl) {
        this.title = title;
        this.movieUrl = movieUrl;
        this.details = details;
        this.participants = participants;
        this.editorId = editorId;
        this.slideShareUrl = slideShareUrl;
    }


    public Conference getConference(List<String> imageUrls, User editor) {
        return Conference.builder()
                .detail(this.details)
                .title(this.title)
                .participants(this.participants)
                .movieUrl(this.movieUrl)
                .editor(editor)
                .imageUrls(imageUrls)
                .slideShareUrl(this.slideShareUrl)
                .build();
    }


    @Getter
    @Setter
    @NoArgsConstructor
    public static class InitConferenceDto{
        private Long conferenceId;
        private boolean published;

        public InitConferenceDto(Conference conference) {
            this.conferenceId = conference.getId();
            this.published = conference.isPublished();
        }
    }
}
