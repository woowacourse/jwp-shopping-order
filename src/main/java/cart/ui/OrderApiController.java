package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrderAddRequest;
import cart.dto.OrderResponse;
import cart.dto.OrdersResponse;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderApiController {
    
    private final OrderService orderService;
    
    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @PostMapping
    public ResponseEntity<OrderResponse> addOrder(Member member, @RequestBody OrderAddRequest request) {
        OrderResponse orderResponse = OrderResponse.of(orderService.addOrder(member, request));
        return ResponseEntity.created(URI.create("/cart-items/orders" + orderResponse.getOrderId())).body(orderResponse);
    }
    
    @GetMapping
    public ResponseEntity<OrdersResponse> findAllOrders(Member member) {
        OrdersResponse ordersResponse= OrdersResponse.of(orderService.findOrdersByMember(member));
        return ResponseEntity.status(HttpStatus.OK).body(ordersResponse);
    }
    
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> findOrder(Member member, @PathVariable Long orderId) {
        OrderResponse orderResponse= OrderResponse.of(orderService.findOrderById(member,orderId));
        return ResponseEntity.status(HttpStatus.OK).body(orderResponse);
    }
    
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(Member member, @PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
    
}
