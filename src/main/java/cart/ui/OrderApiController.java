package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import org.springframework.http.ResponseEntity;
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

  @PostMapping
  public ResponseEntity<OrderResponse> order(Member member, @RequestBody OrderRequest orderRequest) {
    return ResponseEntity.ok(orderService.order(member, orderRequest));
  }
}
