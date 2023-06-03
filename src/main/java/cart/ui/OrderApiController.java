package cart.ui;

import cart.application.OrderService;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.domain.order.OrderProducts;
import cart.dto.order.OrderProductResponse;
import cart.dto.order.OrderProductsRequest;
import cart.dto.order.OrderProductsResponse;
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
    public ResponseEntity<List<OrderProductsResponse>> showOrder(Member member) {
        List<OrderProducts> orderProducts = orderService.getOrderByMember(member);
        return ResponseEntity.ok(toOrderProductsResponse(orderProducts));
    }

    private List<OrderProductsResponse> toOrderProductsResponse(List<OrderProducts> orderProductsOfOrders) {
        return orderProductsOfOrders.stream()
                .map(orderProductsOfOrder -> new OrderProductsResponse(orderProductsOfOrder.getOrderId(), toOrderProductResponse(orderProductsOfOrder.getOrderProducts())))
                .collect(Collectors.toList());
    }

    private List<OrderProductResponse> toOrderProductResponse(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .map(orderProduct -> new OrderProductResponse(
                        orderProduct.getProductId(),
                        orderProduct.getName(),
                        orderProduct.getPrice(),
                        orderProduct.getImageUrl(),
                        orderProduct.getQuantity(),
                        orderProduct.getTotalPrice()
                )).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> showOrderDetail(Member member, @PathVariable long id) {
        Order orderDetail = orderService.getOrderDetailById(member, id);
        return ResponseEntity.ok(toOrderDetailResponse(orderDetail));
    }

    private OrderResponse toOrderDetailResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderProducts(),
                order.getPayment().getTotalPrice(),
                order.getPayment().getUsedPoint(),
                order.getCreatedAt()
        );
    }
}
