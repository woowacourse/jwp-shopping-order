package cart.controller;

import cart.dto.CouponSaveRequest;
import cart.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Operation(summary = "쿠폰 추가", description = "쿠폰을 추가한다.")
    @ApiResponse(
            responseCode = "201"
    )
    @PostMapping
    public ResponseEntity<Void> save(@RequestBody final CouponSaveRequest request) {
        final Long savedId = couponService.save(request);
        return ResponseEntity.created(URI.create("/coupons/" + savedId)).build();
    }
}
