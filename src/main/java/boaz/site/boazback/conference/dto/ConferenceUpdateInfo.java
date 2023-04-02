package boaz.site.boazback.conference.dto;

import boaz.site.boazback.common.domain.URLValid;
import boaz.site.boazback.conference.domain.Conference;
import boaz.site.boazback.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ToString
public class ConferenceUpdateInfo {
    @NotBlank(message = "title must not be blank")
    private String title;
    @NotBlank(message = "movieUrl must not be blank")
    @URLValid(message = "movieUrl is not valid")
    private String movieUrl;
    @NotBlank(message = "details must not be blank")
    private String details;
    @NotBlank(message = "participants must not be blank")
    private String participants;
    @NotBlank(message = "slideShareUrl must not be blank")
    @URLValid(message = "url is not valid")
    private String slideShareUrl;

    @NotNull
    @Size(min = 1,message = "image url is empty")
    private List<@Valid @URLValid(message = "imageUrl is not valid") String> imgUrls;

    @NotBlank
    @URLValid(message = "fileUrl is not valid")
    private String fileUrl;

    @Builder
    public ConferenceUpdateInfo(String title, String movieUrl, String details, String participants, String slideShareUrl, List<String> imgUrls, String fileUrl) {
        this.title = title;
        this.movieUrl = movieUrl;
        this.details = details;
        this.participants = participants;
        this.slideShareUrl = slideShareUrl;
        this.imgUrls = imgUrls;
        this.fileUrl = fileUrl;
    }


    public Conference getConference(User editor) {
        return Conference.builder()
                .detail(this.details)
                .title(this.title)
                .participants(this.participants)
                .movieUrl(this.movieUrl)
                .editor(editor)
                .imageUrls(this.imgUrls)
                .slideShareUrl(this.slideShareUrl)
                .build();
    }
}
