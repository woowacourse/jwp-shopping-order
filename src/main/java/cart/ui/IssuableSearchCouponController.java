package cart.ui;

import cart.application.IssuableSearchCouponService;
import cart.domain.Member;
import cart.dto.IssuableSearchCouponResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/coupons")
public class IssuableSearchCouponController {
    private final IssuableSearchCouponService issuableSearchCouponService;

    public IssuableSearchCouponController(IssuableSearchCouponService issuableSearchCouponService) {
        this.issuableSearchCouponService = issuableSearchCouponService;
    }

    @GetMapping
    public ResponseEntity<List<IssuableSearchCouponResponse>> findAllWithIssuableCondition(Member member) {
        return ResponseEntity.ok(issuableSearchCouponService.findAll(member));
    }
}
