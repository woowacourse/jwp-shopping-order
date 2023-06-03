package cart.ui.order;

import cart.application.service.order.OrderWriteService;
import cart.ui.MemberAuth;
import cart.ui.order.dto.CreateOrderDto;
import cart.ui.order.dto.CreateOrderRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/orders")
public class OrderWriteController {

    private final OrderWriteService orderWriteService;

    public OrderWriteController(OrderWriteService orderWriteService) {
        this.orderWriteService = orderWriteService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(
            final MemberAuth memberAuth,
            @RequestBody CreateOrderRequest createOrderRequest
    ) {
        Long orderId = orderWriteService.createOrder(memberAuth, CreateOrderDto.from(createOrderRequest));
        return ResponseEntity.created(URI.create("orders/" + orderId)).build();
    }
}
