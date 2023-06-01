package cart.controller;

import cart.domain.Member;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.service.order.OrderProvider;
import cart.service.order.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderProvider orderProvider;

    public OrderController(final OrderService orderService, final OrderProvider orderProvider) {
        this.orderService = orderService;
        this.orderProvider = orderProvider;
    }

    @PostMapping
    public ResponseEntity<Void> order(@Valid @RequestBody final OrderRequest orderItemRequest) {
        final Long orderId = orderService.order(orderItemRequest);

        final URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{orderId}")
                .buildAndExpand(orderId)
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrder(final Member member) {
        final List<OrderResponse> orderResponses = orderProvider.findOrderByMember(member);
        return ResponseEntity.ok(orderResponses);
    }
}
