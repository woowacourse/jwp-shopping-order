package cart.controller;

import cart.domain.Member;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.service.OrderService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/orders")
@RestController
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody OrderRequest orderRequest,
                                            @AuthPrincipal Member member) {
        Long id = orderService.register(orderRequest, member);
        return ResponseEntity.created(URI.create("/orders/" + id)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id, @AuthPrincipal Member member) {
        OrderResponse response = orderService.findById(id, member);
        return ResponseEntity.ok().body(response);
    }
}
