package cart.controller;

import cart.domain.Member;
import cart.service.OrderService;
import cart.service.request.OrderRequestDto;
import cart.service.response.OrderResponseDto;
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
public class OrderController {

    private final OrderService orderService;

    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> findOrder(final Member member, final @PathVariable Long id) {
        final OrderResponseDto result = orderService.findOrderById(member, id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> findOrders(final Member member) {
        final List<OrderResponseDto> result = orderService.findOrders(member);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Void> order(final Member member, @RequestBody final OrderRequestDto requestDto) {
        final Long orderId = orderService.order(member, requestDto);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }
}
