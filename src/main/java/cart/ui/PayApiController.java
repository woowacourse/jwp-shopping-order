package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.request.OrderRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class PayApiController {

    private final OrderService orderService;

    public PayApiController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/pay")
    public ResponseEntity<Void> orderCartItems(
            Member member,
            @Valid @RequestBody OrderRequest request
    ) {
        final long orderHistoryId = orderService.orderCartItems(member, request);
        return ResponseEntity
                .created(URI.create("/orders/histories/" + orderHistoryId))
                .build();
    }
}
