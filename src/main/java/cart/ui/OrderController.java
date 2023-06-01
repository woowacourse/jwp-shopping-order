package cart.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;

@RestController
@RequestMapping("/orders")
public class OrderController {
    OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> order(
            @RequestBody OrderRequest orderRequest,
            Member member
    ) {
        final Long orderId = orderService.add(orderRequest, member);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findOrderById(@PathVariable Long id, Member member) {
        final OrderResponse orderResponse = orderService.findById(id, member);
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findOrderByMember(Member member) {
        final List<OrderResponse> orderResponses = orderService.findByMember(member);
        return ResponseEntity.ok(orderResponses);
    }
}
