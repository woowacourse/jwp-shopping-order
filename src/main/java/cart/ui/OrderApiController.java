package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.CartItemResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderApiController {
    private final OrderService orderService;
    
    public OrderApiController(final OrderService orderService) {
        this.orderService = orderService;
    }
    
    @GetMapping
    public ResponseEntity<List<OrderResponse>> addCartItems(Member member) {
        return ResponseEntity.ok(orderService.findByMember(member));
    }
    
    @PostMapping
    public ResponseEntity<Void> addCartItems(Member member, @RequestBody OrderRequest orderRequest) {
        Long orderId = orderService.order(member, orderRequest);
        
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }
}
