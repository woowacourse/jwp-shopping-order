package cart.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrderRequest;

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
        final Long orderId = orderService.order(orderRequest, member);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

}
