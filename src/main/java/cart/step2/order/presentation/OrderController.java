package cart.step2.order.presentation;

import cart.domain.Member;
import cart.step2.coupon.service.CouponService;
import cart.step2.order.presentation.dto.OrderCreateRequest;
import cart.step2.order.presentation.dto.OrderResponse;
import cart.step2.order.service.OrderItemService;
import cart.step2.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RequestMapping("/orders")
@RestController
public class OrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final CouponService couponService;

    public OrderController(final OrderService orderService, final OrderItemService orderItemService, final CouponService couponService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.couponService = couponService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(Member member) {
        List<OrderResponse> responses = orderService.findAllByMemberId(member.getId());
        return ResponseEntity.ok().body(responses);
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(
            Member member,
            @RequestBody final OrderCreateRequest request
    ) {
        Long orderId = orderService.create(member.getId(), request);
        orderItemService.create(member.getId(), request.getCartItemIds(), orderId);

        couponService.changeUsageStatus(member.getId(), request.getCouponId());
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

}
