package boaz.site.boazback.review.dto;

import boaz.site.boazback.review.domain.Review;
import lombok.Builder;
import lombok.Getter;

public class ReviewDto {

    @Getter
    public static class ReviewResponse{
        private String writer;
        private String imageUrl;
        private String track;
        private String company;
        private String job;
        private String year;
        private String contents;

        @Builder(builderMethodName = "transformer",buildMethodName = "transform")
        public ReviewResponse(Review review) {
            this.writer = review.getWriter();
            this.imageUrl = review.getImageUrl();
            this.track = review.getTrack();
            this.company = review.getCompany();
            this.job = review.getJob();
            this.year = review.getYear();
            this.contents = review.getContents();
        }
    }
}
