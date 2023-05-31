package cart.ui.cartitem;

import cart.application.service.cartitem.CartItemReadService;
import cart.application.service.cartitem.dto.CartItemResultDto;
import cart.ui.MemberAuth;
import cart.ui.cartitem.dto.CartItemResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

public class CartItemReadController {

    private final CartItemReadService cartItemReadService;

    public CartItemReadController(final CartItemReadService cartItemReadService) {
        this.cartItemReadService = cartItemReadService;
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> showCartItems(MemberAuth memberAuth) {
        final List<CartItemResultDto> cartItemDtos = cartItemReadService.findByMember(memberAuth);
        final List<CartItemResponse> cartItemResponses = cartItemDtos.stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toUnmodifiableList());
        return ResponseEntity.ok(cartItemResponses);
    }

}
