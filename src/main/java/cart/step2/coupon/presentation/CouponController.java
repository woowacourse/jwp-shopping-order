package cart.step2.coupon.presentation;

import cart.step2.coupon.presentation.dto.CouponResponse;
import cart.step2.coupon.service.CouponService;
import cart.step2.coupontype.service.CouponTypeService;
import cart.domain.Member;
import cart.step2.coupontype.presentation.dto.CouponTypeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;
    private final CouponTypeService couponTypeService;

    public CouponController(final CouponService couponService, final CouponTypeService couponTypeService) {
        this.couponService = couponService;
        this.couponTypeService = couponTypeService;
    }

    @PostMapping("/{couponTypeId}")
    public ResponseEntity<Void> createCoupon(Member member, @PathVariable Long couponTypeId) {
        Long couponId = couponService.createCoupon(member.getId(), couponTypeId);
        return ResponseEntity.created(URI.create("/coupons/" + couponId)).build();
    }

    @PatchMapping("/{couponId}")
    public ResponseEntity<Void> issuanceCoupon(Member member, @PathVariable Long couponId) {
        couponService.addCoupon(member.getId(), couponId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CouponTypeResponse>> showAllCouponsTypes() {
        List<CouponTypeResponse> couponsType = couponTypeService.getCouponsType();
        return ResponseEntity.ok().body(couponsType);
    }

    @GetMapping("/member")
    public ResponseEntity<List<CouponResponse>> showMemberCoupons(Member member) {
        List<CouponResponse> coupons = couponService.getMemberCoupons(member.getId());
        return ResponseEntity.ok().body(coupons);
    }

    @DeleteMapping("/{couponId}")
    public ResponseEntity<Void> deleteCoupon(Member member, @PathVariable Long couponId) {
        couponService.deleteByCouponId(couponId);
        return ResponseEntity.noContent().build();
    }

}
