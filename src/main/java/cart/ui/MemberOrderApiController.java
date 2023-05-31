package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.response.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/members/orders")
public class MemberOrderApiController {

    private final OrderService orderService;

    public MemberOrderApiController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(Member member) {
        return ResponseEntity.ok(orderService.getAllOrdersOf(member));
    }
}
