package cart.ui;

import cart.application.CouponService;
import cart.domain.Member;
import cart.dto.OrderCouponResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final CouponService couponService;

    public OrderApiController(final CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping("/coupons")
    public ResponseEntity<List<OrderCouponResponse>> findCoupons(
            @RequestParam final List<Long> cartItemId,
            Member member
    ) {
        List<OrderCouponResponse> orderCouponResponses = couponService.calculateCouponForCarts(member, cartItemId);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderCouponResponses);
    }
}
