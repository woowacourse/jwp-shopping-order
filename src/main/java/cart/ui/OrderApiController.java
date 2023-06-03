package cart.ui;

import cart.application.dto.order.CreateOrderByCartItemsRequest;
import cart.application.dto.order.FindOrderCouponsResponse;
import cart.application.dto.order.FindOrderDetailResponse;
import cart.application.dto.order.FindOrdersResponse;
import cart.application.service.MemberCouponService;
import cart.application.service.OrderService;
import cart.domain.Member;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;
    private final MemberCouponService memberCouponService;

    public OrderApiController(final OrderService orderService,
            final MemberCouponService memberCouponService) {
        this.orderService = orderService;
        this.memberCouponService = memberCouponService;
    }

    @GetMapping("/coupons")
    public ResponseEntity<FindOrderCouponsResponse> getCouponsByCartItemIds(Member member,
            @RequestParam(name = "cartItemId") final List<Long> cartItemIds) {
        return ResponseEntity.ok(memberCouponService.findOrderCoupons(member, cartItemIds));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FindOrderDetailResponse> findOrderDetail(Member member, @PathVariable Long id) {
        return ResponseEntity.ok(orderService.findOrderById(member, id));
    }

    @GetMapping
    public ResponseEntity<FindOrdersResponse> findAllOrders(Member member) {
        return ResponseEntity.ok(orderService.findOrders(member));
    }

    @PostMapping
    public ResponseEntity<Void> orderCartItems(Member member,
            @RequestBody CreateOrderByCartItemsRequest request) {
        long id = orderService.orderCartItems(member, request);
        return ResponseEntity.created(URI.create("/orders/" + id)).build();
    }
}
