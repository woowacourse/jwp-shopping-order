package cart.ui;

import cart.application.CartItemService;
import cart.application.MemberService;
import cart.domain.Member;
import cart.domain.Point;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.dto.CartResponse;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart-items")
public class CartItemApiController {

    private final CartItemService cartItemService;
    private final MemberService memberService;

    public CartItemApiController(CartItemService cartItemService, MemberService memberService) {
        this.cartItemService = cartItemService;
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<CartResponse> showCart(Member member) {
        List<CartItemResponse> cartItems = cartItemService.findByMember(member);
        Point memberPoint = memberService.getMemberPoint(member);
        Point minUsagePoint = Point.MINIMUM_USAGE_POINT;
        return ResponseEntity.ok(new CartResponse(cartItems, memberPoint.getValue(), minUsagePoint.getValue()));
    }

    @PostMapping
    public ResponseEntity<Void> addCartItems(Member member, @RequestBody CartItemRequest cartItemRequest) {
        Long cartItemId = cartItemService.add(member, cartItemRequest);

        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCartItemQuantity(Member member, @PathVariable Long id,
                                                       @RequestBody CartItemQuantityUpdateRequest request) {
        cartItemService.updateQuantity(member, id, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItems(Member member, @PathVariable Long id) {
        cartItemService.remove(member, id);

        return ResponseEntity.noContent().build();
    }
}
