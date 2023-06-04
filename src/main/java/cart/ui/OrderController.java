package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.DetailOrderResponse;
import cart.dto.OrderPageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<OrderPageResponse> getOrderPages(Member member, @RequestParam("orderPage") Integer orderPage) {
        OrderPageResponse orders = orderService.findOrders(member, orderPage);
        return ResponseEntity.ok().body(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<DetailOrderResponse> getDetailOrder(Member member, @PathVariable Long orderId) {
        DetailOrderResponse detailOrder = orderService.findDetailOrder(member, orderId);
        return ResponseEntity.ok(detailOrder);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(Member member, @PathVariable Long orderId) {
        orderService.deleteOrder(member, orderId);
        return ResponseEntity.ok().body("ok");
    }
}
