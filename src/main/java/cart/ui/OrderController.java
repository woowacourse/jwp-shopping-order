package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.response.OrderResponse;
import cart.dto.request.OrderRequest;
import cart.dto.response.CouponConfirmResponse;
import cart.dto.response.OrdersResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> saveOrder(Member member, @RequestBody OrderRequest orderRequest) {
        Long savedId = orderService.save(member, orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + savedId)).build();
    }

    @GetMapping
    public ResponseEntity<List<OrdersResponse>> getOrders(Member member) {
        List<OrdersResponse> orders = orderService.findAllByMemberId(member);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(Member member, @PathVariable("id") Long id) {
        OrderResponse order = orderService.findByOrderId(member, id);
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(Member member, @PathVariable("id") Long id) {
        orderService.cancelOrder(member, id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<CouponConfirmResponse> confirmOrder(Member member, @PathVariable("id") Long id) {
        CouponConfirmResponse response = orderService.confirmOrder(member, id);
        return ResponseEntity.ok(response);
    }
}
