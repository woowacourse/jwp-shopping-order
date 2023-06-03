package cart.ui.cartitem;

import cart.application.service.cartitem.CartItemReadService;
import cart.application.service.cartitem.dto.CartResultDto;
import cart.application.service.order.OrderMakeService;
import cart.application.service.order.dto.CreateOrderDto;
import cart.domain.member.Member;
import cart.ui.cartitem.dto.CartPaymentResponse;
import cart.ui.cartitem.dto.CartResponse;
import cart.ui.order.dto.request.CreateOrderRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart-items")
public class CartItemReadController {

    private final CartItemReadService cartItemReadService;
    private final OrderMakeService orderMakeService;

    public CartItemReadController(
            final CartItemReadService cartItemReadService,
            final OrderMakeService orderMakeService
    ) {
        this.cartItemReadService = cartItemReadService;
        this.orderMakeService = orderMakeService;
    }

    @GetMapping
    public ResponseEntity<CartResponse> showCartItems(final Member memberAuth) {
        final CartResultDto cartResult = cartItemReadService.findByMember(memberAuth);
        return ResponseEntity.ok(CartResponse.from(cartResult));
    }

    @PostMapping("/payment")
    public ResponseEntity<CartPaymentResponse> calculatePrice(
            final Member member,
            @RequestBody CreateOrderRequest createOrderRequest
    ) {
        final int paymentPrice = orderMakeService.getPaymentPrice(member, CreateOrderDto.from(createOrderRequest));
        return ResponseEntity.ok(new CartPaymentResponse(paymentPrice));
    }

}
