package cart.ui;

import cart.application.CouponService;
import cart.domain.Member;
import cart.dto.request.CouponCreateRequest;
import cart.dto.response.CouponIssuableResponse;
import cart.dto.response.CouponResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/users/coupons")
    public ResponseEntity<Void> save(Member member, @RequestBody CouponCreateRequest couponCreateRequest) {
        Long savedId = couponService.save(member, couponCreateRequest);
        return ResponseEntity.created(URI.create("/coupons/" + savedId)).build();
    }

    @GetMapping("/users/coupons")
    public ResponseEntity<List<CouponResponse>> findByMemberId(Member member) {
        List<CouponResponse> coupons = couponService.findByMemberId(member);
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("/coupons")
    public ResponseEntity<List<CouponIssuableResponse>> findAll(Member member) {
        List<CouponIssuableResponse> coupons = couponService.findAll(member);
        return ResponseEntity.ok(coupons);
    }
}
