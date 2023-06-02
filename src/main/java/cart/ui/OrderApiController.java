package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.domain.OrderHistory;
import cart.dto.order.OrderDetailResponse;
import cart.dto.order.OrderHistoriesResponse;
import cart.dto.order.OrderProductsRequest;
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

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<OrderHistoriesResponse> showOrderProductHistories(Member member) {
        List<OrderHistory> orderHistories = orderService.getOrderItems(member);
        return ResponseEntity.ok(toOrderHistoryResponse(orderHistories));
    }

    private OrderHistoriesResponse toOrderHistoryResponse(List<OrderHistory> orderHistories) {
        System.out.println("\n!!!! MemberArgumentResolver.toOrderHistoryResponse() 반환값 변경 필요\n");
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> showOrderHistoryById(Member member, @PathVariable long id) {
        List<OrderDetailResponse> orderDetails = orderService.getOrderItemDetailById(member, id);
        return ResponseEntity.ok(toOrderDetailResponse(orderDetails));
    }

    private OrderDetailResponse toOrderDetailResponse(List<OrderDetailResponse> orderDetailResponses) {
        System.out.println("\n!!!! MemberArgumentResolver.toOrderDetailResponse() 반환값 변경 필요\n");
        return null;
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
