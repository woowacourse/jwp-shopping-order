package cart.application.cart;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cart.application.cart.dto.CartDto;
import cart.application.cart.dto.CartItemDto;
import cart.domain.cart.Cart;
import cart.domain.cart.CartItem;
import cart.domain.cart.CartItemRepository;
import cart.domain.cart.CartRepository;
import cart.domain.product.Product;
import cart.domain.product.ProductRepository;
import cart.error.exception.CartItemException;

@Service
public class CartService {
	private final ProductRepository productRepository;
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;

	public CartService(final ProductRepository productRepository, final CartRepository cartRepository,
		final CartItemRepository cartItemRepository) {
		this.productRepository = productRepository;
		this.cartRepository = cartRepository;
		this.cartItemRepository = cartItemRepository;
	}

	public List<CartItemDto> findByMember(Long memberId) {
		final Cart cart = cartRepository.findByMemberId(memberId);
		final List<CartItem> cartItems = cart.getCartItems();

		return cartItems.stream()
			.map(CartItemDto::from)
			.collect(Collectors.toList());
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
