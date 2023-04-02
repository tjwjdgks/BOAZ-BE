package boaz.site.boazback.review.application;

import boaz.site.boazback.review.domain.Review;
import boaz.site.boazback.review.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public List<ReviewDto.ReviewResponse> getPaginationReviews(Pageable pageable) {
        log.info("getPaginationReviews Service start");
        List<Review> reviewList = reviewRepository.findByOrderByIdDesc(pageable);
        List<ReviewDto.ReviewResponse> responseList = reviewList.stream()
                .map(ReviewDto.ReviewResponse::new)
                .collect(Collectors.toList());
        log.info("getPaginationReviews Service end");
        return responseList;
    }

    @Override
    public int getPaginationCountAboutReview(int size) {
        log.info("getPaginationCountAboutReview Service start");
        Long cnt = reviewRepository.count();
        int pages = cnt == 0 ? 1 : (int) Math.ceil((double) cnt / (double) size);
        log.info("getPaginationCountAboutReview Service end");
        return pages;
    }
}
