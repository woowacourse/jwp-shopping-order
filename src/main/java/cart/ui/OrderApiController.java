package cart.ui;

import cart.application.CouponService;
import cart.domain.Member;
import cart.dto.OrderCouponResponse;
import cart.dto.OrderRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<Void> order(
            @RequestBody OrderRequest request,
            Member member
    ) {
        // TODO: 5/30/23 서비스 호출 추가
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }
}
