package cart.ui.cartitem;

import cart.application.service.cartitem.CartItemReadService;
import cart.application.service.cartitem.CartItemWriteService;
import cart.application.service.cartitem.dto.CartItemCreateDto;
import cart.application.service.cartitem.dto.CartItemUpdateDto;
import cart.application.service.cartitem.dto.CartResultDto;
import cart.application.service.order.OrderMakeService;
import cart.application.service.order.dto.CreateOrderDto;
import cart.domain.member.Member;
import cart.ui.cartitem.dto.CartItemQuantityUpdateRequest;
import cart.ui.cartitem.dto.CartItemRequest;
import cart.ui.cartitem.dto.CartPaymentResponse;
import cart.ui.cartitem.dto.CartResponse;
import cart.ui.order.dto.request.CreateOrderRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/cart-items")
public class CartItemController {

    private final CartItemReadService cartItemReadService;
    private final OrderMakeService orderMakeService;
    private final CartItemWriteService cartItemWriteService;

    public CartItemController(
            final CartItemReadService cartItemReadService,
            final OrderMakeService orderMakeService,
            final CartItemWriteService cartItemWriteService) {
        this.cartItemReadService = cartItemReadService;
        this.orderMakeService = orderMakeService;
        this.cartItemWriteService = cartItemWriteService;
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

    @PostMapping
    public ResponseEntity<Void> addCartItems(Member memberAuth, @RequestBody CartItemRequest cartItemRequest) {

        final Long cartItemId = cartItemWriteService.createCartItem(memberAuth, CartItemCreateDto.from(cartItemRequest));

        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCartItemQuantity(
            Member memberAuth,
            @PathVariable("id") Long cartItemId,
            @RequestBody CartItemQuantityUpdateRequest cartItemQuantityUpdateRequest
    ) {

        cartItemWriteService.updateQuantity(memberAuth, cartItemId, CartItemUpdateDto.from(cartItemQuantityUpdateRequest));

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItems(Member memberAuth, @PathVariable Long id) {
        cartItemWriteService.remove(memberAuth, id);

        return ResponseEntity.noContent().build();
    }

}
