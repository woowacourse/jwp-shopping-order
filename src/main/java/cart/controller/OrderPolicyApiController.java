package cart.controller;

import cart.service.OrderPolicyService;
import cart.dto.orderpolicy.OrderPolicyResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order-policy")
public class OrderPolicyApiController {

    private OrderPolicyService orderPolicyService;

    public OrderPolicyApiController(OrderPolicyService orderPolicyService) {
        this.orderPolicyService = orderPolicyService;
    }

    @GetMapping
    public ResponseEntity<OrderPolicyResponse> showOrders() {
        return ResponseEntity.ok(orderPolicyService.findOrderPolicy());
    }
}

