package cart.ui;

import cart.application.OrderService;
import cart.dto.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/{memberId}/orders/{orderId}")
    public ResponseEntity<OrderResponse> getOrderByIds(@PathVariable Long memberId, @PathVariable Long orderId){
        return ResponseEntity.ok(orderService.getOrderByIds(memberId, orderId));
    }

    @GetMapping("/{memberId}/orders")
    public ResponseEntity<List<OrderResponse>> getAllOrders(@PathVariable Long memberId){
        return ResponseEntity.ok(orderService.getAllOrders(memberId));
    }
}
