package cart.controller;

import cart.auth.Auth;
import cart.auth.Credential;
import cart.dto.coupon.CouponResponse;
import cart.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "basicAuth")
@Tag(name = "쿠폰", description = "쿠폰을 관리한다.")
@RequestMapping("/coupons")
@RestController
public class CouponController {

    private final CouponService couponService;

    public CouponController(final CouponService couponService) {
        this.couponService = couponService;
    }

    @Operation(summary = "전체 쿠폰 조회", description = "전체 쿠폰을 조회한다.")
    @ApiResponse(
            responseCode = "200",
            description = "전체 쿠폰 조회 성공.",
            content = @Content(schema = @Schema(implementation = CouponResponse.class))
    )
    @GetMapping
    public ResponseEntity<List<CouponResponse>> findAll(@Auth final Credential credential) {
        final List<CouponResponse> couponResponses = couponService.findAllByMemberId(
                credential.getMemberId());
        return ResponseEntity.ok(couponResponses);
    }
}
