package cart.ui.order;

import cart.application.service.order.OrderWriteService;
import cart.domain.member.Member;
import cart.application.service.order.dto.CreateOrderDto;
import cart.ui.order.dto.request.CreateOrderRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/orders")
public class OrderWriteController {

    private final OrderWriteService orderWriteService;

    public OrderWriteController(final OrderWriteService orderWriteService) {
        this.orderWriteService = orderWriteService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(
            final Member member,
            @RequestBody final CreateOrderRequest createOrderRequest
    ) {
        final Long orderId = orderWriteService.createOrder(member, CreateOrderDto.from(createOrderRequest));

        final String createOrderUri = generateCreateUri(orderId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, createOrderUri)
                .build();
    }

    private String generateCreateUri(final Long lineId) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(lineId)
                .toUriString();
    }

}
