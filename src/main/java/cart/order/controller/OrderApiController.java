package cart.order.controller;

import cart.member.domain.Member;
import cart.order.application.OrderCommandService;
import cart.order.application.OrderQueryService;
import cart.order.application.dto.OrderResponse;
import cart.order.application.dto.RegisterOrderRequest;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderApiController {

  private final OrderCommandService orderCommandService;
  private final OrderQueryService orderQueryService;

  public OrderApiController(
      final OrderCommandService orderCommandService,
      final OrderQueryService orderQueryService
  ) {
    this.orderCommandService = orderCommandService;
    this.orderQueryService = orderQueryService;
  }

  @PostMapping("/orders")
  public ResponseEntity<Void> registerOrder(
      final Member member,
      @RequestBody final RegisterOrderRequest registerOrderRequest
  ) {
    Long savedId = orderCommandService.registerOrder(member, registerOrderRequest);

    return ResponseEntity.created(URI.create("/orders/" + savedId)).build();
  }

  @GetMapping("/orders")
  public ResponseEntity<List<OrderResponse>> showOrders(final Member member) {
    return ResponseEntity.ok(orderQueryService.searchOrders(member));
  }
}
