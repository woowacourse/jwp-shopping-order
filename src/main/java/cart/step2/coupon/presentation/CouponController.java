package cart.step2.coupon.presentation;

import cart.domain.Member;
import cart.step2.coupon.domain.Coupon;
import cart.step2.coupon.presentation.dto.CouponResponse;
import cart.step2.coupon.service.CouponService;
import cart.step2.coupontype.domain.CouponType;
import cart.step2.coupontype.presentation.dto.CouponTypeResponse;
import cart.step2.coupontype.service.CouponTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;
    private final CouponTypeService couponTypeService;

    public CouponController(final CouponService couponService, final CouponTypeService couponTypeService) {
        this.couponService = couponService;
        this.couponTypeService = couponTypeService;
    }

    @PostMapping
    public ResponseEntity<Void> createCoupon(Member member, @RequestParam Long couponTypeId) {
        Long couponId = couponService.createCoupon(member.getId(), couponTypeId);
        return ResponseEntity.created(URI.create("/coupons/" + couponId)).build();
    }

    @PatchMapping
    public ResponseEntity<Void> issuanceCoupon(Member member, @RequestParam Long couponId) {
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
        List<Coupon> coupons = couponService.getMemberCoupons(member.getId());
        List<CouponResponse> responses = coupons.stream()
                .map(coupon -> {
                    CouponType couponType = couponTypeService.getCoupon(coupon.getCouponTypeId());
                    return new CouponResponse(coupon, couponType);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(responses);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUsedCoupon(Member member, @RequestParam Long couponId) {
        couponService.deleteUsedCouponByCouponId(couponId);
        return ResponseEntity.noContent().build();
    }

}
