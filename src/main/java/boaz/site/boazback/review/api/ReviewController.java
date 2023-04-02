package boaz.site.boazback.review.api;

import boaz.site.boazback.common.domain.Result;
import boaz.site.boazback.review.application.ReviewService;
import boaz.site.boazback.review.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

@GetMapping
public Result getReviews(@PageableDefault(page = 0,size = 40,sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
    log.info("getReviews API start");
    Result result = new Result().resultSuccess();
    List<ReviewDto.ReviewResponse> reviewResponseList = reviewService.getPaginationReviews(pageable);
    result.setData(reviewResponseList);
    log.info("getReviews API end");
    return result;
}


    @GetMapping("/pages")
    public Result getTotalPagesAboutReviews(@RequestParam("size") int size) {
        log.info("getTotalPagesAboutReviews API start");
        Result result = new Result().resultSuccess();
        int totalPages = reviewService.getPaginationCountAboutReview(size);
        result.setData(totalPages);
        log.info("getTotalPagesAboutReviews API end");
        return result;
    }
}
