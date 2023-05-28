package cart.controller;

import cart.auth.Auth;
import cart.auth.Credential;
import cart.dto.OrderSaveRequest;
import cart.dto.OrdersDto;
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
public class OrderController {

    private final OrderService orderService;

    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> order(@Auth final Credential credential, @RequestBody final OrderSaveRequest request) {
        Long orderId = orderService.order(credential.getMemberId(), request);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @GetMapping
    public ResponseEntity<List<OrdersDto>> findAllOrderByMemberId(@Auth final Credential credential) {
        List<OrdersDto> findAllOrders = orderService.findAllBy(credential.getMemberId());
        return ResponseEntity.ok(findAllOrders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrdersDto> findOrderByMemberId(@Auth Credential credential, @PathVariable Long orderId) {
        OrdersDto orderDto = orderService.findByOrderId(credential.getMemberId(), orderId);
        return ResponseEntity.ok(orderDto);
    }
}
