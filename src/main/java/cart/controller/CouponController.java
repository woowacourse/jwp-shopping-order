package cart.controller;

import cart.auth.Auth;
import cart.auth.Credential;
import cart.dto.CouponSaveRequest;
import cart.dto.MemberCouponDto;
import cart.service.CouponService;
import cart.service.MemberCouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final MemberCouponService memberCouponService;

    public CouponController(final CouponService couponService, final MemberCouponService memberCouponService) {
        this.couponService = couponService;
        this.memberCouponService = memberCouponService;
    }

    @Operation(summary = "쿠폰 추가", description = "쿠폰을 추가한다.")
    @ApiResponse(
            responseCode = "201"
    )
    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody final CouponSaveRequest request) {
        final Long savedId = couponService.save(request);
        return ResponseEntity.created(URI.create("/coupons/" + savedId)).build();
    }

    @Operation(summary = "사용자 쿠폰 전체 조회", description = "사용되지 않은 사용자 쿠폰 전체를 조회한다.")
    @ApiResponse(
            responseCode = "200",
            description = "사용자 쿠폰 전체 조회 성공"
    )
    @GetMapping
    public ResponseEntity<List<MemberCouponDto>> findAll(@Auth final Credential credential) {
        final List<MemberCouponDto> couponDtos = memberCouponService.findByMemberId(credential.getMemberId());
        return ResponseEntity.ok(couponDtos);
    }
}
