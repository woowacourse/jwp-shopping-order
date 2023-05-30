package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.request.CartItemRequest;
import cart.dto.request.OrderRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> saveOrder(Member member, @RequestBody OrderRequest orderRequest) {
        long orderId = orderService.saveOrder(member, orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

}
