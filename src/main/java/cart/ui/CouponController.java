package cart.ui;

import cart.application.CouponService;
import cart.domain.Member;
import cart.dto.request.CouponCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/users/coupons")
    public ResponseEntity<Void> publishUserCoupon(Member member, @RequestBody CouponCreateRequest couponCreateRequest) {
        Long savedId = couponService.publishUserCoupon(member, couponCreateRequest);
        return ResponseEntity.created(URI.create("/coupons/" + savedId)).build();
    }

}
