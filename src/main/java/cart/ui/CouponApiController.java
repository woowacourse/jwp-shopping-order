package cart.ui;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cart.application.coupon.CouponQueryService;
import cart.application.coupon.dto.CouponResponse;
import cart.config.auth.LoginMember;
import cart.config.auth.dto.AuthMember;

@RestController
@RequestMapping("/coupons")
public class CouponApiController {

	private final CouponQueryService couponQueryService;

	public CouponApiController(final CouponQueryService couponQueryService) {
		this.couponQueryService = couponQueryService;
	}

	@CrossOrigin(origins = {"https://feb-dain.github.io", "http://localhost:3000"},
		allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH,
		RequestMethod.DELETE, RequestMethod.OPTIONS},
		allowCredentials = "true", exposedHeaders = "Location")
	@GetMapping
	public ResponseEntity<List<CouponResponse>> showCoupons(@LoginMember AuthMember member) {
		List<CouponResponse> couponResponses = couponQueryService.findByMemberId(member.getId());
		return ResponseEntity.ok(couponResponses);
	}
}
