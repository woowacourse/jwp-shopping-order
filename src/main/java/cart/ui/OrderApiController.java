package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.request.PayRequest;
import cart.dto.response.PayResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/pay")
    public ResponseEntity<PayResponse> orderCartItems(
            Member member,
            @Valid @RequestBody PayRequest request
    ) {
        final PayResponse response = orderService.orderCartItems(member, request);
        return ResponseEntity
                .ok()
                .body(response);
    }
}
