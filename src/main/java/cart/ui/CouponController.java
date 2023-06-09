package cart.ui;

import cart.application.CouponWithIssueStateService;
import cart.domain.member.Member;
import cart.dto.IssuableSearchCouponResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/coupons")
public class CouponController {
    private final CouponWithIssueStateService couponWithIssueStateService;

    public CouponController(CouponWithIssueStateService couponWithIssueStateService) {
        this.couponWithIssueStateService = couponWithIssueStateService;
    }

    @GetMapping
    public ResponseEntity<List<IssuableSearchCouponResponse>> findAllWithIssuableCondition(Member member) {
        return ResponseEntity.ok(couponWithIssueStateService.findAll(member));
    }
}
