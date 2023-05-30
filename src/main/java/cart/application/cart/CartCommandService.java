package cart.application.cart;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.application.cart.dto.CartDto;
import cart.application.cart.dto.CartItemDto;
import cart.domain.cart.Cart;
import cart.domain.cart.CartItem;
import cart.domain.cart.CartItemRepository;
import cart.domain.cart.CartRepository;
import cart.domain.product.Product;
import cart.domain.product.ProductRepository;
import cart.error.exception.CartItemException;

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

	public Long add(CartDto cartDto) {
		final CartItemDto cartItemDto = cartDto.getCartItemDto();
		final Product product = productRepository.findById(cartItemDto.getProductId())
			.orElseThrow();
		final CartItem cartItem = new CartItem(null, product, cartItemDto.getQuantity());

		final Cart cart = new Cart(cartDto.getMemberId(), List.of(cartItem));
		return cartRepository.save(cart);
	}

	public void updateQuantity(CartDto cartDto) {
		final Cart cart = cartRepository.findByMemberId(cartDto.getMemberId());

		final CartItemDto cartItemDto = cartDto.getCartItemDto();
		final CartItem cartItem = cart.findCartItemById(cartItemDto.getId())
			.orElseThrow(() -> new CartItemException.IllegalMember(cartDto.getMemberId()));

		cartItem.changeQuantity(cartItemDto.getQuantity());

		if (cartItem.getQuantity() == 0) {
			cartItemRepository.deleteById(cartItem.getId());
			return;
		}

		cartItemRepository.updateQuantity(cartItem);
	}

	public void remove(CartDto cartDto) {
		final Cart cart = cartRepository.findByMemberId(cartDto.getMemberId());

		final CartItemDto cartItemDto = cartDto.getCartItemDto();
		final CartItem cartItem = cart.findCartItemById(cartItemDto.getId())
			.orElseThrow(() -> new CartItemException.IllegalMember(cartDto.getMemberId()));

		cartItemRepository.deleteById(cartItem.getId());
	}
}
