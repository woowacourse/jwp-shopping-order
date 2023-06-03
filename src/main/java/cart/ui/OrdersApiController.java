package cart.ui;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cart.application.AddOrderService;
import cart.application.FindOrderService;
import cart.application.dto.GetDetailedOrderResponse;
import cart.application.dto.GetOrdersRequest;
import cart.application.dto.GetOrdersResponse;
import cart.application.dto.PostOrderRequest;
import cart.application.event.CancelOrderService;
import cart.domain.Member;

@RestController
public class OrdersApiController {

    private final FindOrderService findOrderService;
    private final AddOrderService addOrderService;
    private final CancelOrderService cancelOrderService;

    public OrdersApiController(FindOrderService findOrderService, AddOrderService addOrderService,
        CancelOrderService cancelOrderService) {
        this.findOrderService = findOrderService;
        this.addOrderService = addOrderService;
        this.cancelOrderService = cancelOrderService;
    }

    @GetMapping("/orders")
    public ResponseEntity<GetOrdersResponse> getOrdersWithPagination(Member member,
        @ModelAttribute @Valid GetOrdersRequest request) {
        return ResponseEntity.ok(findOrderService.getOrdersWithPagination(member, request));
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<GetDetailedOrderResponse> getDetailedOrder(Member member, @PathVariable Long orderId) {
        return ResponseEntity.ok(findOrderService.getOrder(member, orderId));
    }

    @PostMapping("/orders")
    public ResponseEntity<Void> addOrder(Member member, @RequestBody @Valid PostOrderRequest request) {
        Long orderId = addOrderService.addOrder(member, request);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<Void> cancel(Member member, @PathVariable Long orderId) {
        cancelOrderService.cancelOrder(member, orderId);
        return ResponseEntity.ok().build();
    }
}
