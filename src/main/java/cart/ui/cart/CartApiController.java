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

import cart.application.cart.CartCommandService;
import cart.application.cart.CartQueryService;
import cart.application.cart.dto.CarItemAddDto;
import cart.application.cart.dto.CartItemUpdateQuantityDto;
import cart.application.cart.dto.CartItemsRemoveDto;
import cart.domain.member.Member;
import cart.ui.cart.dto.CartItemQuantityUpdateRequest;
import cart.ui.cart.dto.CartItemRemoveRequest;
import cart.ui.cart.dto.CartItemRequest;
import cart.ui.cart.dto.CartItemResponse;

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
	public ResponseEntity<List<CartItemResponse>> showCartItems(Member member) {
		final List<CartItemResponse> cartItemResponses =
			cartQueryService.findByMemberId(member.getId()).stream()
				.map(CartItemResponse::from)
				.collect(Collectors.toList());

		return ResponseEntity.ok(cartItemResponses);
	}

	@PostMapping
	public ResponseEntity<Void> addCartItems(Member member, @RequestBody CartItemRequest cartItemRequest) {
		final Long cartItemId = cartCommandService.add(
			new CarItemAddDto(member.getId(), cartItemRequest.getProductId()));
		return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Void> updateCartItemQuantity(Member member, @PathVariable Long id,
		@RequestBody CartItemQuantityUpdateRequest request) {
		cartCommandService.updateQuantity(new CartItemUpdateQuantityDto(member.getId(), id, request.getQuantity()));
		return ResponseEntity.ok().build();
	}

	@DeleteMapping
	public ResponseEntity<Void> removeCartItems(Member member,
		@RequestBody CartItemRemoveRequest cartItemRemoveRequest) {
		cartCommandService.remove(new CartItemsRemoveDto(member.getId(), cartItemRemoveRequest.getCartItemIds()));
		return ResponseEntity.noContent().build();
	}
}
