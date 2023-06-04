package cart.ui;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

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

import cart.application.cart.CartCommandService;
import cart.application.cart.CartQueryService;
import cart.application.cart.dto.CartItemQuantityUpdateRequest;
import cart.application.cart.dto.CartItemRemoveRequest;
import cart.application.cart.dto.CartItemRequest;
import cart.application.cart.dto.CartItemResponse;
import cart.config.auth.LoginMember;
import cart.config.auth.dto.AuthMember;

@CrossOrigin(origins = {"https://feb-dain.github.io", "http://localhost:3000"},
	allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH,
	RequestMethod.DELETE, RequestMethod.OPTIONS},
	allowCredentials = "true", exposedHeaders = "Location")
@RestController
@RequestMapping("/cart-items")
public class CartApiController {

	private final CartCommandService cartCommandService;
	private final CartQueryService cartQueryService;

	public CartApiController(final CartCommandService cartCommandService, final CartQueryService cartQueryService) {
		this.cartCommandService = cartCommandService;
		this.cartQueryService = cartQueryService;
	}

	@GetMapping
	public ResponseEntity<List<CartItemResponse>> showCartItems(@LoginMember AuthMember member) {
		final List<CartItemResponse> cartItemResponses = cartQueryService.findByMemberId(member.getId());

		return ResponseEntity.ok(cartItemResponses);
	}

	@PostMapping
	public ResponseEntity<Void> addCartItems(@LoginMember AuthMember member,
		@Valid @RequestBody CartItemRequest cartItemRequest) {
		final Long cartItemId = cartCommandService.add(member.getId(), cartItemRequest);
		return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Void> updateCartItemQuantity(@LoginMember AuthMember member, @PathVariable Long id,
		@RequestBody CartItemQuantityUpdateRequest request) {
		cartCommandService.updateQuantity(member.getId(), id, request);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping
	public ResponseEntity<Void> removeCartItems(@LoginMember AuthMember member,
		@RequestBody CartItemRemoveRequest cartItemRemoveRequest) {
		cartCommandService.remove(member.getId(), cartItemRemoveRequest);
		return ResponseEntity.noContent().build();
	}
}
