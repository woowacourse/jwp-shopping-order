package cart.controller;

import cart.dto.MemberInfo;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.service.OrderService;
import java.net.URI;
import java.util.List;
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
    public ResponseEntity<Void> createOrder(@AuthPrincipal MemberInfo member, @RequestBody OrderRequest orderRequest) {
        Long id = orderService.register(orderRequest, member);
        return ResponseEntity.created(URI.create("/orders/" + id)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> getOrder(@AuthPrincipal MemberInfo member, @PathVariable Long id) {
        OrderDetailResponse response = orderService.findById(id, member);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(@AuthPrincipal MemberInfo member) {
        List<OrderResponse> responses = orderService.findAll(member);
        return ResponseEntity.ok().body(responses);
    }
}
