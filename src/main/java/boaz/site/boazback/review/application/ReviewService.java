package boaz.site.boazback.review.application;

import boaz.site.boazback.review.dto.ReviewDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {
    List<ReviewDto.ReviewResponse> getPaginationReviews(Pageable pageable);

    int getPaginationCountAboutReview(int size);
}
