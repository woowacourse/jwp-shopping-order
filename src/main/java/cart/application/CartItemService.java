package cart.application;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.request.CartItemAddRequest;
import cart.dto.response.CartItemResponse;
import cart.dto.request.CartItemUpdateRequest;
import cart.dto.response.CartItemUpdateResponse;
import cart.entity.ProductEntity;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartItemService {
	private final ProductDao productDao;
	private final CartItemDao cartItemDao;

	public CartItemService(ProductDao productDao, CartItemDao cartItemDao) {
		this.productDao = productDao;
		this.cartItemDao = cartItemDao;
	}

	public Long add(Member member, CartItemAddRequest cartItemAddRequest) {
		final ProductEntity productEntity = productDao.getProductById(cartItemAddRequest.getProductId());
		final Product product = Product.from(productEntity);
		return cartItemDao.save(new CartItem(member, product));
	}

	@Transactional(readOnly = true)
	public List<CartItemResponse> findByMember(Member member) {
		List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
		return CartItemResponse.ofCartItems(cartItems);
	}

	@Transactional(readOnly = true)
	public CartItemResponse findById(Long cartItemId) {
		final CartItem cartItem = cartItemDao.findById(cartItemId);
		return CartItemResponse.of(cartItem);
	}

	public CartItemUpdateResponse updateQuantity(Member member, Long id, CartItemUpdateRequest request) {
		CartItem cartItem = cartItemDao.findById(id);
		cartItem.checkOwner(member);

		if (request.getQuantity() == 0) {
			cartItemDao.deleteById(id);
			return new CartItemUpdateResponse(0, false);
		}

		cartItem.changeCartItem(request.getQuantity(), request.isChecked());
		cartItemDao.update(cartItem);
		return new CartItemUpdateResponse(cartItem.getQuantity(), cartItem.isChecked());
	}

	public void remove(Member member, Long id) {
		CartItem cartItem = cartItemDao.findById(id);
		cartItem.checkOwner(member);

		cartItemDao.deleteById(id);
	}
}
