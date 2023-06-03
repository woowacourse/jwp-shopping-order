package cart.presentation.controller;

import cart.application.OrderService;
import cart.application.domain.Member;
import cart.presentation.dto.request.AuthInfo;
import cart.presentation.dto.request.OrderRequest;
import cart.presentation.dto.response.OrderResponse;
import cart.presentation.dto.response.SpecificOrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(AuthInfo authInfo) {
        List<OrderResponse> responses = orderService.getAllOrders(authInfo);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecificOrderResponse> getSpecificOrder(@PathVariable Long id, AuthInfo authInfo) {
        SpecificOrderResponse response = orderService.getSpecificOrder(authInfo, id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> issueOrder(AuthInfo authInfo, @RequestBody OrderRequest request) {
        long id = orderService.issue(authInfo, request);
        return ResponseEntity.created(URI.create("/orders/" + id)).build();
    }
}
