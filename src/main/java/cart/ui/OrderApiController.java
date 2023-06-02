package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItems;
import cart.dto.order.OrderItemsResponse;
import cart.dto.order.OrderProductsRequest;
import cart.dto.order.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<OrderItemsResponse>> showOrder(Member member) {
        List<OrderItems> orderItems = orderService.getOrderByMember(member);
        return ResponseEntity.ok(toOrderResponse(orderItems));
    }

    private List<OrderItemsResponse> toOrderResponse(List<OrderItems> orderItems) {
        return orderItems.stream()
                .map(orderItem -> new OrderItemsResponse(orderItem.getOrderId(), orderItem.getOrderItems()))
                .collect(Collectors.toList());
    }
}
