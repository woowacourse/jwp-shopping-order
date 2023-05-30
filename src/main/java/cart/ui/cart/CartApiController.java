package cart.ui.cart;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cart.application.cart.CartService;
import cart.application.cart.dto.CartDto;
import cart.application.cart.dto.CartItemDto;
import cart.domain.member.Member;
import cart.ui.cart.dto.CartItemQuantityUpdateRequest;
import cart.ui.cart.dto.CartItemRequest;
import cart.ui.cart.dto.CartItemResponse;

@CrossOrigin(origins = {"https://feb-dain.github.io", "http://localhost:3000"},
	allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH,
	RequestMethod.DELETE, RequestMethod.OPTIONS},
	allowCredentials = "true", exposedHeaders = "Location")
@RestController
@RequestMapping("/cart-items")
public class CartApiController {

	private final CartService cartService;

	public CartApiController(CartService cartService) {
		this.cartService = cartService;
	}

	@GetMapping
	public ResponseEntity<List<CartItemResponse>> showCartItems(Member member) {
		final List<CartItemResponse> cartItemResponses =
			cartService.findByMember(member.getId()).stream()
				.map(CartItemResponse::from)
				.collect(Collectors.toList());

		return ResponseEntity.ok(cartItemResponses);
	}

	@PostMapping
	public ResponseEntity<Void> addCartItems(Member member, @RequestBody CartItemRequest cartItemRequest) {
		final CartItemDto cartItemDto = new CartItemDto(cartItemRequest);

		final CartDto cartDto = new CartDto(member.getId(), cartItemDto);

		Long cartItemId = cartService.add(cartDto);

		return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Void> updateCartItemQuantity(Member member, @PathVariable Long id,
		@RequestBody CartItemQuantityUpdateRequest request) {
		final CartDto cartDto = new CartDto(member.getId(), new CartItemDto(id, request.getQuantity(), null));
		cartService.updateQuantity(cartDto);

		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> removeCartItems(Member member, @PathVariable Long id) {
		final CartDto cartDto = new CartDto(member.getId(), new CartItemDto(id, 0, null));
		cartService.remove(cartDto);

		return ResponseEntity.noContent().build();
	}
}
