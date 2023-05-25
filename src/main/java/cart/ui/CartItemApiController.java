package cart.ui;

import java.net.URI;
import java.util.List;

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

import cart.application.CartItemService;
import cart.domain.Member;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;

@CrossOrigin(origins = {"https://feb-dain.github.io/react-shopping-cart-prod/", "http://localhost:3000"},
	allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH,
	RequestMethod.DELETE, RequestMethod.OPTIONS},
	allowCredentials = "true", exposedHeaders = "Location")
@RestController
@RequestMapping("/cart-items")
public class CartItemApiController {

	private final CartItemService cartItemService;

	public CartItemApiController(CartItemService cartItemService) {
		this.cartItemService = cartItemService;
	}

	@GetMapping
	public ResponseEntity<List<CartItemResponse>> showCartItems(Member member) {
		return ResponseEntity.ok(cartItemService.findByMember(member));
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
