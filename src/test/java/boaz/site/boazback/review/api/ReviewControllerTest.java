package boaz.site.boazback.review.api;

import boaz.site.boazback.BaseControllerTest;
import boaz.site.boazback.review.domain.Review;
import boaz.site.boazback.review.dto.ReviewDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class ReviewControllerTest extends BaseControllerTest {

    List<ReviewDto.ReviewResponse> reviewResponseList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Review fakeReview1 = Review.builder()
                .id(1L)
                .writer("taebin")
                .build();
        ReviewDto.ReviewResponse fakeResponse1 = ReviewDto.ReviewResponse.transformer()
                .review(fakeReview1)
                .transform();
        reviewResponseList.add(fakeResponse1);
    }


    @Test
    void getTotalPagesAboutReviews() throws Exception {
        given(reviewService.getPaginationCountAboutReview(anyInt())).willReturn(5);
        mockMvc.perform(get("/reviews/pages")
                        .param("size", "5"))
                .andDo(print())
                .andExpect(jsonPath("$.payload").value(5));
    }

    @Test
    void getReviews() throws Exception {
        given(reviewService.getPaginationReviews(any())).willReturn(reviewResponseList);
        mockMvc.perform(get("/reviews")
                .param("page", "0")
                .param("size", "5"))
                .andDo(print())
                .andExpect(jsonPath("$.payload").isArray())
                .andExpect(jsonPath("$.payload.[0].writer").value("taebin"))
        ;
    }
}
