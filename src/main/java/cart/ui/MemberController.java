package cart.ui;

import cart.application.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/{memberId}/orders/{orderId}")
    public ResponseEntity<OrderResponse> getOrderByOrderId(@PathVariable Long memberId, @PathVariable Long orderId){
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }
}
