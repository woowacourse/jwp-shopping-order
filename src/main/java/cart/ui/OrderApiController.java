package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.AllOrderResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

  private final OrderService orderService;

  public OrderApiController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping
  public ResponseEntity<List<AllOrderResponse>> getAllOrders(Member member) {
    return ResponseEntity.ok(orderService.getAllOrders(member));
  }

  @GetMapping("/{id}")
  public ResponseEntity<AllOrderResponse> getOrder(Member member, @PathVariable Long id) {
    return ResponseEntity.ok(orderService.getOrder(id));
  }

  @PostMapping
  public ResponseEntity<OrderResponse> order(Member member, @RequestBody OrderRequest orderRequest) {
    return ResponseEntity.ok(orderService.order(member, orderRequest));
  }
}
