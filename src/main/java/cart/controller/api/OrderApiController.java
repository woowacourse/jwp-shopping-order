package cart.controller.api;

import cart.controller.AuthPrincipal;
import cart.domain.Member;
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
    public ResponseEntity<Void> register(@RequestBody OrderRequest orderRequest,
                                         @AuthPrincipal Member member) {
        Long id = orderService.register(orderRequest, member);
        return ResponseEntity.created(URI.create("/orders/" + id)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> find(@PathVariable Long id, @AuthPrincipal Member member) {
        OrderDetailResponse response = orderService.findById(id, member);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findAll(@AuthPrincipal Member member) {
        List<OrderResponse> responses = orderService.findAll(member);
        return ResponseEntity.ok().body(responses);
    }
}
