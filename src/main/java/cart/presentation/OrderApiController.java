package cart.presentation;

import cart.domain.Member;
import cart.dto.OrderRequest;
import cart.service.OrderService;
import java.net.URI;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderApiController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Void> order(final Member member, final @RequestBody OrderRequest orderRequest) {
        long orderId = orderService.order(member, orderRequest);

        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }
}
