package cart.controller;

import cart.dto.coupon.CouponSaveRequest;
import cart.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "basicAuth")
@Tag(name = "쿠폰 발급", description = "쿠폰을 발급한다.")
@RequestMapping("/issuance")
@RestController
public class CouponIssuanceController {

    private final CouponService couponService;

    public CouponIssuanceController(final CouponService couponService) {
        this.couponService = couponService;
    }

    @Operation(summary = "쿠폰 발급", description = "쿠폰을 모든 사용자에게 발급한다.")
    @ApiResponse(
            responseCode = "200",
            description = "전체 쿠폰 발급 성공.",
            content = @Content(schema = @Schema(implementation = CouponSaveRequest.class))
    )
    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody final CouponSaveRequest couponSaveRequest) {
        couponService.issuance(couponSaveRequest);
        return ResponseEntity.ok().build();
    }
}
