package cart.ui;

import cart.application.OrderPolicyService;
import cart.dto.response.OrderPolicyResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order-policy")
public class OrderPolicyApiController {

    private final OrderPolicyService orderPolicyService;

    public OrderPolicyApiController(final OrderPolicyService orderPolicyService) {
        this.orderPolicyService = orderPolicyService;
    }

    @GetMapping
    public ResponseEntity<OrderPolicyResponse> findOrderPolicy() {
        OrderPolicyResponse response = orderPolicyService.find();
        return ResponseEntity.ok().body(response);
    }
}
