package boaz.site.boazback.curriculum.dto;

import boaz.site.boazback.curriculum.domain.MemberSuggestion;
import lombok.Getter;

@Getter
public class MemberSuggestionDto {

    private String writer;
    private String year;

    private String track;

    private String emojiUrl;

    private String content;

    public MemberSuggestionDto(MemberSuggestion memberSuggestion) {
        this.writer = memberSuggestion.getWriter();
        this.year = memberSuggestion.getYear();
        this.track = memberSuggestion.getTrack();
        this.emojiUrl = memberSuggestion.getEmojiUrl();
        this.content = memberSuggestion.getContent();
    }
}
