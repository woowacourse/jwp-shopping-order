package cart.controller.order;

import cart.config.auth.Auth;
import cart.domain.Member;
import cart.dto.order.OrderReqeust;
import cart.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/payments")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> payment(@Auth Member member, @RequestBody OrderReqeust orderReqeust) {
        Long id = orderService.order(member, orderReqeust);
        return ResponseEntity.created(URI.create("/payments/" + id)).build();
    }
}
