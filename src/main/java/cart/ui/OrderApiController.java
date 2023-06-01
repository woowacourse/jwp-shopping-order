package cart.ui;

import cart.application.CouponService;
import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.*;
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
        Long orderId = orderService.createOrder(member, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(URI.create("/orders/" + orderId))
                .build();
    }

    @GetMapping
    public ResponseEntity<AllOrderResponse> findAllOrders(Member member) {
        AllOrderResponse allOrderResponse = orderService.findAllOrderByMember(member);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(allOrderResponse);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> findOrderDetail(
            Member member,
            @PathVariable Long orderId
    ) {
        OrderDetailResponse orderDetailResponse = orderService.findOrderByIdAndMember(orderId, member);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderDetailResponse);
    }

    @GetMapping("/coupons")
    public ResponseEntity<AllOrderCouponResponse> findCoupons(
            @RequestParam final List<Long> cartItemId,
            Member member
    ) {
        AllOrderCouponResponse allOrderCouponResponse = couponService.calculateCouponForCarts(member, cartItemId);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(allOrderCouponResponse);
    }
}
