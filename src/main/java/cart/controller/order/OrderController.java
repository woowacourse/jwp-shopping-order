package cart.controller.order;

import cart.config.auth.Auth;
import cart.domain.Member;
import cart.domain.order.Order;
import cart.dto.order.OrderRequest;
import cart.dto.order.PreparedOrderResponse;
import cart.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/payments")
    public ResponseEntity<Void> payment(@Auth Member member, @RequestBody OrderRequest orderRequest) {
        Long id = orderService.doOrder(member, orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + id)).build();
    }

    @GetMapping("/cart-items/coupons")
    public ResponseEntity<PreparedOrderResponse> applyCoupons(@Auth Member member, @RequestParam(name = "id") List<Long> couponIds) {
        PreparedOrderResponse preparedOrderResponse = orderService.prepareOrder(member, couponIds);
        return ResponseEntity.ok().body(preparedOrderResponse);
    }
}
