package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.order.OrderRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> addOrder(Member member, @RequestBody OrderRequest orderRequest) {
        Long id = orderService.addOrder(member, orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + id)).build();
    }
}
