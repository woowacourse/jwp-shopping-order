package cart.controller;

import cart.auth.Auth;
import cart.domain.member.Member;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.dto.SpecificOrderResponse;
import cart.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> addOrder(@Auth final Member member, @RequestBody final OrderRequest orderRequest) {
        final Long orderId = orderService.addOrder(member, orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(@Auth final Member member) {
        orderService.getAllOrders(member);
        return null;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<SpecificOrderResponse> getOrder(@Auth final Member member, @PathVariable final Long orderId) {
        orderService.getOrderById(member, orderId);
        return null;
    }

}
