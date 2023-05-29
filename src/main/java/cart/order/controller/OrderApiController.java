package cart.order.controller;

import cart.member.domain.Member;
import cart.order.application.OrderCommandService;
import cart.order.application.dto.RegisterOrderRequest;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderApiController {

  private final OrderCommandService orderCommandService;

  public OrderApiController(final OrderCommandService orderCommandService) {
    this.orderCommandService = orderCommandService;
  }

  @PostMapping("/orders")
  public ResponseEntity<Void> registerOrder(
      final Member member,
      @RequestBody final RegisterOrderRequest registerOrderRequest
  ) {
    Long savedId = orderCommandService.registerOrder(member, registerOrderRequest);

    return ResponseEntity.created(URI.create("/orders/" + savedId)).build();
  }
}
