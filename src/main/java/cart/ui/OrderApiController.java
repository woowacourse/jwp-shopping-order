package cart.ui;

import cart.application.OrderService;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.dto.order.OrderProductsRequest;
import cart.dto.order.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

import static java.util.stream.Collectors.toList;

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
    public ResponseEntity<List<OrderResponse>> showOrder(Member member) {
        List<Order> orders = orderService.getOrderByMember(member);
        List<OrderResponse> orderResponses = orders.stream()
                .map(this::toOrderResponse)
                .collect(toList());
        return ResponseEntity.ok(orderResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> showOrderDetail(Member member, @PathVariable long id) {
        Order orderDetail = orderService.getOrderDetailById(member, id);
        return ResponseEntity.ok(toOrderResponse(orderDetail));
    }

    private OrderResponse toOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderProducts(),
                order.getPayment().getTotalPrice(),
                order.getPayment().getUsedPoint(),
                order.getCreatedAt()
        );
    }
}
