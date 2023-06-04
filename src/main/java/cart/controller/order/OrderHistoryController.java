package cart.controller.order;


import cart.config.auth.Auth;
import cart.domain.Member;
import cart.dto.order.OrderDetailHistoryResponse;
import cart.dto.order.OrderHistoryResponse;
import cart.service.OrderHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderHistoryController {
    private final OrderHistoryService orderHistoryService;

    public OrderHistoryController(OrderHistoryService orderHistoryService) {
        this.orderHistoryService = orderHistoryService;
    }

    @GetMapping
    public ResponseEntity<List<OrderHistoryResponse>> readOrderHistories(@Auth Member member) {
        List<OrderHistoryResponse> orderHistoryResponses = orderHistoryService.readOrderHistory(member);
        return ResponseEntity.ok().body(orderHistoryResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailHistoryResponse> readOrderDetailHistory(@Auth Member member, @PathVariable Long id) {
        OrderDetailHistoryResponse orderDetailHistoryResponse = orderHistoryService.readDetailHistory(member, id);
        return ResponseEntity.ok().body(orderDetailHistoryResponse);
    }

}
