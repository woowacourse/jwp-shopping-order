package cart.ui;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cart.application.coupon.CouponCommandService;
import cart.application.coupon.CouponQueryService;
import cart.application.coupon.dto.CouponRequest;
import cart.application.coupon.dto.CouponResponse;
import cart.config.auth.LoginMember;
import cart.config.auth.dto.AuthMember;

@RestController
@RequestMapping("/coupons")
public class CouponApiController {

	private final CouponQueryService couponQueryService;
	private final CouponCommandService couponCommandService;

	public CouponApiController(final CouponQueryService couponQueryService,
		final CouponCommandService couponCommandService) {
		this.couponQueryService = couponQueryService;
		this.couponCommandService = couponCommandService;
	}

	@CrossOrigin(origins = {"https://feb-dain.github.io", "https://cruelladevil.github.io", "http://localhost:3000"},
		allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH,
		RequestMethod.DELETE, RequestMethod.OPTIONS},
		allowCredentials = "true", exposedHeaders = "Location")
	@GetMapping
	public ResponseEntity<List<CouponResponse>> showMemberCoupons(@LoginMember AuthMember member) {
		List<CouponResponse> couponResponses = couponQueryService.findByMemberId(member.getId());
		return ResponseEntity.ok(couponResponses);
	}

	@PostMapping
	public ResponseEntity<Void> createCoupon(@Valid @RequestBody CouponRequest request) {
		final Long savedId = couponCommandService.createCoupon(request);
		return ResponseEntity.created(URI.create("/coupons/" + savedId)).build();
	}

	@PostMapping("/{id}")
	public ResponseEntity<Void> addCoupon(@LoginMember AuthMember member, @PathVariable Long id) {
		final Long savedId = couponCommandService.addCoupon(member.getId(), id);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateCoupon(@PathVariable Long id, @Valid @RequestBody CouponRequest request) {
		couponCommandService.updateCoupon(id, request);
		return ResponseEntity.ok().build();
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Void> generateExtraCoupons(@PathVariable Long id, @Valid @RequestBody CouponRequest request) {
		couponCommandService.generateExtraCoupons(id, request);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
		couponCommandService.removeCoupon(id);
		return ResponseEntity.noContent().build();
	}
}
