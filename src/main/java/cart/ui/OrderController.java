package cart.ui;

import cart.application.OrderService;
import cart.application.dto.order.OrderRefundResponse;
import cart.application.dto.order.OrderRequest;
import cart.application.dto.order.OrderResponse;
import cart.common.auth.MemberName;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> orderProducts(@MemberName final String memberName,
                                              @RequestBody final OrderRequest orderRequest) {
        final Long orderId = orderService.orderProduct(memberName, orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@MemberName final String memberName,
                                                  @PathVariable final Long id) {
        final OrderResponse orderResponse = orderService.getOrderById(memberName, id);
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(@MemberName final String memberName) {
        final List<OrderResponse> orderResponses = orderService.getOrders(memberName);
        return ResponseEntity.ok(orderResponses);
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<OrderRefundResponse> cancelOrder(@MemberName final String memberName,
                                                           @PathVariable final Long id) {
        final OrderRefundResponse orderRefundResponse = orderService.cancelOrder(memberName, id);
        return ResponseEntity.ok(orderRefundResponse);
    }
}
