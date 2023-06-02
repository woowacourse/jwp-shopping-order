package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> order(@RequestBody final OrderRequest request, final Member member) {
        final OrderResponse response = orderService.order(request, member);
        return ResponseEntity.created(URI.create("/order/" + response.getId()))
            .body(response);
    }
}
