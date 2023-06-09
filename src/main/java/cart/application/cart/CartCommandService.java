package cart.application.cart;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.application.cart.dto.CartItemQuantityUpdateRequest;
import cart.application.cart.dto.CartItemRemoveRequest;
import cart.application.cart.dto.CartItemRequest;
import cart.domain.cart.Cart;
import cart.domain.cart.CartItem;
import cart.domain.cart.CartItemRepository;
import cart.domain.cart.CartRepository;
import cart.domain.product.Product;
import cart.domain.product.ProductRepository;
import cart.error.exception.BadRequestException;
import cart.error.exception.ForbiddenException;

@Transactional
@Service
public class CartCommandService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ProductRepository productRepository;

	public CartCommandService(final ProductRepository productRepository, final CartRepository cartRepository,
		final CartItemRepository cartItemRepository) {
		this.productRepository = productRepository;
		this.cartRepository = cartRepository;
		this.cartItemRepository = cartItemRepository;
	}

	public Long add(final Long memberId, final CartItemRequest request) {
		final Product product = productRepository.findById(request.getProductId())
			.orElseThrow(BadRequestException.Product::new);

		return addProductOrIncreaseQuantity(memberId, product);
	}

	public void updateQuantity(final Long memberId, final Long cartItemId,
		final CartItemQuantityUpdateRequest request) {
		final Cart cart = cartRepository.findByMemberId(memberId);
		final CartItem cartItem = cart.getCartItem(cartItemId)
			.orElseThrow(ForbiddenException.Cart::new);
		cartItem.changeQuantity(request.getQuantity());

		if (cartItem.getQuantity() == 0) {
			cartItemRepository.deleteByIds(List.of(cartItem.getId()));
			return;
		}

		cartItemRepository.updateQuantity(cartItem);
	}

	public void remove(final Long memberId, final CartItemRemoveRequest request) {
		final Cart cart = cartRepository.findByMemberId(memberId);

		if (cart.contains(request.getCartItemIds())) {
			cartItemRepository.deleteByIds(request.getCartItemIds());
			return;
		}
		throw new ForbiddenException.Cart();
	}

	private Long addProductOrIncreaseQuantity(final Long memberId, final Product product) {
		final Cart cart = cartRepository.findByMemberId(memberId);
		return cart.getCartItemByProductId(product.getId())
			.map(this::increaseQuantity)
			.orElseGet(() -> createNewCartItem(memberId, product));
	}

	private Long createNewCartItem(final Long memberId, final Product product) {
		final CartItem cartItem = new CartItem(product);
		final Cart cart = new Cart(memberId, List.of(cartItem));
		return cartRepository.save(cart);
	}

	private Long increaseQuantity(final CartItem cartItem) {
		cartItem.changeQuantity(cartItem.getQuantity() + 1);
		cartItemRepository.updateQuantity(cartItem);
		return cartItem.getId();
	}
}
