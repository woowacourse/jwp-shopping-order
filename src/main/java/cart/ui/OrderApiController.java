package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.response.OrderHistoryResponse;
import cart.dto.response.OrderProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/members/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderHistoryResponse>> getAllOrders(Member member) {
        return ResponseEntity.ok(orderService.getOrderHistoriesOf(member));
    }

    @GetMapping("/{orderHistoryId}")
    public ResponseEntity<List<OrderProductResponse>> getOrderProductByOrderId(Member member, @PathVariable Long orderHistoryId) {
        return ResponseEntity.ok().body(orderService.getOrderProductsOf(member, orderHistoryId));
    }
}
