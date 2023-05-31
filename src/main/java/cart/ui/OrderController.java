package cart.ui;

import cart.application.OrderService;
import cart.application.dto.order.OrderRequest;
import cart.common.auth.MemberName;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> orderProducts(@MemberName final String memberName,
                                              @RequestBody final OrderRequest orderRequest) {
        final Long orderId = orderService.orderProduct(memberName, orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }
}
