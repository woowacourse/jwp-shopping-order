package cart.ui;

import cart.application.CouponService;
import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrderCouponResponse;
import cart.dto.OrderRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderApiController {
    private final CouponService couponService;
    private final OrderService orderService;

    public OrderApiController(final CouponService couponService, final OrderService orderService) {
        this.couponService = couponService;
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> order(
            @RequestBody OrderRequest request,
            Member member
    ) {
        Long orderId = orderService.order(member, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(URI.create("/orders/" + orderId))
                .build();
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
