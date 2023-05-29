package cart.controller;

import cart.auth.Auth;
import cart.auth.Credential;
import cart.dto.CouponResponse;
import cart.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SecurityRequirement(name = "basicAuth")
@Tag(name = "쿠폰", description = "쿠폰을 조회한다")
@RequestMapping("/coupons")
@RestController
public class CouponController {

    private final CouponService couponService;

    public CouponController(final CouponService couponService) {
        this.couponService = couponService;
    }

    @Operation(summary = "사용자 쿠폰 조회", description = "사용자의 모든 쿠폰을 조회한다.")
    @ApiResponse(
            responseCode = "200",
            description = "사용자 쿠폰 조회 성공"
    )
    @GetMapping
    public ResponseEntity<List<CouponResponse>> findAll(@Auth final Credential credential) {
        final List<CouponResponse> result = couponService.findAllByMemberId(credential.getMemberId());
        return ResponseEntity.ok(result);
    }
}
