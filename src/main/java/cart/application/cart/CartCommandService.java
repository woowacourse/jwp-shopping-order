package cart.application.cart;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.application.cart.dto.CarItemAddDto;
import cart.application.cart.dto.CartItemUpdateQuantityDto;
import cart.application.cart.dto.CartItemsRemoveDto;
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

	public Long add(CarItemAddDto cartDto) {
		final Product product = productRepository.findById(cartDto.getProductId())
			.orElseThrow();

		return cartRepository.save(new Cart(cartDto.getMemberId(), List.of(new CartItem(product))));
	}

	public void updateQuantity(CartItemUpdateQuantityDto cartDto) {
		final Cart cart = cartRepository.findByMemberId(cartDto.getMemberId());

		final CartItem cartItem = cart.findCartItemById(cartDto.getCartItemId())
			.orElseThrow(() -> new CartItemException.IllegalMember(cartDto.getMemberId()));

		cartItem.changeQuantity(cartDto.getQuantity());

		if (cartItem.getQuantity() == 0) {
			cartItemRepository.deleteByIds(List.of(cartItem.getId()));
			return;
		}

		cartItemRepository.updateQuantity(cartItem);
	}

	public void remove(CartItemsRemoveDto cartDto) {
		final Cart cart = cartRepository.findByMemberId(cartDto.getMemberId());

		if (cart.contains(cartDto.getCartItemIds())) {
			cartItemRepository.deleteByIds(cartDto.getCartItemIds());
			return;
		}
		throw new CartItemException.IllegalMember(cartDto.getMemberId());
	}
}
