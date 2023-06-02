package cart.ui;

import java.net.URI;

import cart.application.OrderService;
import cart.config.AuthPrincipal;
import cart.dto.AuthMember;
import cart.dto.OrderRequest;
import cart.dto.OrdersResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> order(@AuthPrincipal AuthMember authMember, @RequestBody OrderRequest orderRequest) {
        Long orderedId = orderService.order(authMember, orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + orderedId)).build();
    }

    @GetMapping
    public ResponseEntity<OrdersResponse> showOrders(@AuthPrincipal AuthMember authMember) {
        OrdersResponse ordersResponse = orderService.findOrders(authMember);
        return ResponseEntity.ok(ordersResponse);
    }
}
