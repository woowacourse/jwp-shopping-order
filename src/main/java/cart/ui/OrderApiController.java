package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrderAddRequest;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart-items")
public class OrderApiController {
    
    private final OrderService orderService;
    
    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @PostMapping("/order")
    public ResponseEntity<Void> addOrder(Member member, @RequestBody OrderAddRequest request) {
        Long orderId = orderService.addOrder(member, request);
        return ResponseEntity.created(URI.create("/cart-items/orders" + orderId)).build();
    }
    
}
