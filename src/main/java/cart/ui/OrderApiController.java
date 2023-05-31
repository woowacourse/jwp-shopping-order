package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrderAddRequest;
import cart.dto.OrdersResponse;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<Void> addOrder(Member member, @RequestBody OrderAddRequest request) {
        Long orderId = orderService.addOrder(member, request);
        return ResponseEntity.created(URI.create("/cart-items/orders" + orderId)).build();
    }
    
    @GetMapping
    public ResponseEntity<OrdersResponse> findAllOrders(Member member) {
        OrdersResponse ordersResponse= OrdersResponse.of(orderService.findOrdersByMember(member));
        return ResponseEntity.status(HttpStatus.OK).body(ordersResponse);
    }
    
}
