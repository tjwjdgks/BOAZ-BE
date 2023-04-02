package boaz.site.boazback.review.application;

import boaz.site.boazback.BaseDataJpaTest;
import boaz.site.boazback.review.domain.Review;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ReviewRepositoryTest extends BaseDataJpaTest {

    @Test
    void findByOrderByIdDesc() {
        Pageable pageable = PageRequest.of(0, 5);
        List<Review> result = reviewRepository.findByOrderByIdDesc(pageable);
        assertThat(result).hasSize(3);
        System.out.println("result = " + result);
    }
}
