package cart.cartitem.ui;

import cart.cartitem.application.CartItemService;
import cart.cartitem.domain.CartItem;
import cart.cartitem.ui.request.CartItemQuantityUpdateRequest;
import cart.cartitem.ui.request.CartItemRequest;
import cart.cartitem.ui.response.CartItemResponse;
import cart.member.domain.Member;
import cart.product.application.ProductService;
import cart.product.domain.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cart-items")
public class CartItemApiController {

    private final CartItemService cartItemService;
    private final ProductService productService;

    public CartItemApiController(final CartItemService cartItemService, final ProductService productService) {
        this.cartItemService = cartItemService;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> showCartItems(final Member member) {
        final List<CartItem> cartItems = cartItemService.findByMember(member);

        final List<CartItemResponse> responses = cartItems.stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<Void> addCartItems(final Member member,
                                             @Valid @RequestBody final CartItemRequest cartItemRequest) {
        final Product product = productService.getProductById(cartItemRequest.getProductId());
        final CartItem cartItem = CartItem.of(member, product);
        final Long cartItemId = cartItemService.add(cartItem);

        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCartItemQuantity(final Member member,
                                                       @PathVariable final Long id,
                                                       @Valid @RequestBody final CartItemQuantityUpdateRequest request) {
        final int quantity = request.getQuantity();

        cartItemService.updateQuantity(member, id, quantity);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItems(final Member member,
                                                @PathVariable final Long id) {
        cartItemService.remove(member, id);

        return ResponseEntity.noContent().build();
    }
}
