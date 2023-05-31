package cart.ui.controller;

import cart.application.OrderService;
import cart.domain.member.Member;
import cart.ui.controller.dto.response.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/orders")
@RestController
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderDetail(Member member, @PathVariable Long id) {
        OrderResponse response = orderService.getOrderDetail(id, member);
        return ResponseEntity.ok(response);
    }
}
