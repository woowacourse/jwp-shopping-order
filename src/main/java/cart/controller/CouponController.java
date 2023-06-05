package cart.controller;

import cart.auth.Authenticate;
import cart.auth.Credentials;
import cart.controller.dto.CouponResponse;
import cart.controller.dto.CouponTypeResponse;
import cart.service.coupon.CouponProvider;
import cart.service.coupon.CouponService;
import cart.service.dto.CouponReissueRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@SecurityRequirement(name = "basicAuth")
@Tag(name = "쿠폰", description = "쿠폰을 관리한다.")
@RestController
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;
    private final CouponProvider couponProvider;

    public CouponController(final CouponService couponService, final CouponProvider couponProvider) {
        this.couponService = couponService;
        this.couponProvider = couponProvider;
    }

    @Operation(summary = "쿠폰 발급", description = "사용자에게 쿠폰을 발급한다.")
    @ApiResponse(
            responseCode = "201",
            description = "쿠폰 발급 성공"
    )
    @PostMapping("/{couponId}")
    public ResponseEntity<Void> issueCoupon(@Authenticate final Credentials credentials, @PathVariable Long couponId) {
        final Long memberCouponId = couponService.issueCoupon(credentials, couponId);

        return ResponseEntity.created(URI.create(String.format("/coupons/%s", memberCouponId))).build();
    }

    @Operation(summary = "쿠폰 재발급", description = "이미 사용한 쿠폰을 사용자에게 재발급한다.")
    @ApiResponse(
            responseCode = "200",
            description = "쿠폰 재발급 성공"
    )
    @PatchMapping("/{couponId}")
    public ResponseEntity<Void> reissueCoupon(@PathVariable final Long couponId, @Valid @RequestBody final CouponReissueRequest request) {
        couponService.reissueCoupon(couponId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "쿠폰 전체 조회", description = "전체 쿠폰을 조회한다.")
    @ApiResponse(
            responseCode = "200",
            description = "쿠폰 전체 조회 성공"
    )
    @GetMapping
    public ResponseEntity<List<CouponTypeResponse>> getAllCoupons() {
        final List<CouponTypeResponse> couponResponses = couponProvider.findCouponAll();
        return ResponseEntity.ok(couponResponses);
    }

    @Operation(summary = "사용자 쿠폰 전체 조회", description = "사용자가 소유한 전체 쿠폰을 조회한다.")
    @ApiResponse(
            responseCode = "200",
            description = "사용자 쿠폰 전체 조회 성공"
    )
    @GetMapping("/member")
    public ResponseEntity<List<CouponResponse>> getMemberCoupons(@Authenticate final Credentials credentials) {
        final List<CouponResponse> couponResponses = couponProvider.findCouponByMember(credentials);
        return ResponseEntity.ok(couponResponses);
    }

    @Operation(summary = "사용한 쿠폰 삭제", description = "사용한 쿠폰을 삭제한다.")
    @ApiResponse(
            responseCode = "204",
            description = "사용자 쿠폰 삭제 성공"
    )
    @DeleteMapping("/{couponId}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable final Long couponId) {
        couponService.deleteCoupon(couponId);
        return ResponseEntity.noContent().build();
    }
}
