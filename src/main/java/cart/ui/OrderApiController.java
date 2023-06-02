package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.domain.OrderItems;
import cart.dto.order.OrderItemsResponse;
import cart.dto.order.OrderProductsRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.net.URI;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> orderProducts(Member member, @RequestBody @NotNull OrderProductsRequest orderProductsRequest) {
        long orderId = orderService.orderProducts(member, orderProductsRequest);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @GetMapping
    public ResponseEntity<OrderItemsResponse> showOrder(Member member) {
        OrderItems orderItems = orderService.getOrderByMember(member);
        return ResponseEntity.ok(toOrderResponse(orderItems));
    }

    private OrderItemsResponse toOrderResponse(OrderItems orderItems) {
        return new OrderItemsResponse(orderItems.getOrderId(), orderItems.getOrderItems());
    }
}
