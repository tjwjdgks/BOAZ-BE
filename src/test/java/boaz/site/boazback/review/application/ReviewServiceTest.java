package boaz.site.boazback.review.application;


import boaz.site.boazback.review.domain.Review;
import boaz.site.boazback.review.dto.ReviewDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    List<Review> reviewList = new ArrayList<>();
    @BeforeEach
    void setUp() {
        reviewService = new ReviewServiceImpl(reviewRepository);
        Review fakeReview1 = Review.builder()
                .id(1L)
                .writer("1")
                .build();
        Review fakeReview2 = Review.builder()
                .id(2L)
                .writer("2")
                .build();
        Review fakeReview3 = Review.builder()
                .id(3L)
                .writer("3")
                .build();
        reviewList.add(fakeReview1);
        reviewList.add(fakeReview2);
        reviewList.add(fakeReview3);
    }

    @Test
    void getReviews() {
        given(reviewRepository.findByOrderByIdDesc(any())).willReturn(reviewList);
        Pageable pageable = PageRequest.of(0, 5);
        List<ReviewDto.ReviewResponse> reviews = reviewService.getPaginationReviews(pageable);
        assertThat(reviews).hasSize(3);
    }

    @Test
    void getPaginationCountAboutReview(){
        int size = 5;
        given(reviewRepository.count()).willReturn(25L);
        int count = reviewService.getPaginationCountAboutReview(size);
        assertThat(count).isEqualTo(size);
    }

}
