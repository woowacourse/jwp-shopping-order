package cart.application;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.exception.BusinessException;
import cart.ui.dto.request.CartItemQuantityUpdateRequest;
import cart.ui.dto.request.CartItemRequest;
import cart.ui.dto.request.RemoveCartItemsRequest;
import cart.ui.dto.response.CartItemResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CartItemService {

    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public CartItemService(final ProductDao productDao, final CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    public List<CartItemResponse> findByMember(final Member member) {
        final List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        return cartItems.stream().map(CartItemResponse::of).collect(Collectors.toList());
    }

    @Transactional
    public Long add(final Member member, final CartItemRequest cartItemRequest) {
        final Optional<CartItem> optionalCartItem = cartItemDao.findByMemberIdAndProductId(member.getId(),
            cartItemRequest.getProductId());
        if (optionalCartItem.isPresent()) {
            final CartItem cartItem = optionalCartItem.get();
            final CartItem updatedCartItem = cartItem.addQuantity(cartItemRequest.getQuantity());
            cartItemDao.updateQuantity(updatedCartItem);
            return cartItem.getId();
        }
        final Product product = productDao.getProductById(cartItemRequest.getProductId())
            .orElseThrow(() -> new BusinessException("존재하지 않는 상품입니다."));
        return cartItemDao.save(
            new CartItem(cartItemRequest.getQuantity(), product, member));
    }

    @Transactional
    public void updateQuantity(final Member member, final Long id, final CartItemQuantityUpdateRequest request) {
        final CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemDao.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemDao.updateQuantity(cartItem);
    }

    @Transactional
    public void remove(final Member member, final Long id) {
        final CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        cartItemDao.deleteById(id);
    }

    @Transactional
    public void remove(final Member member, final RemoveCartItemsRequest request) {
        final List<CartItem> cartItems = cartItemDao.findAllByIds(request.getCartItemIds());
        for (final CartItem cartItem : cartItems) {
            cartItem.checkOwner(member);
        }
        cartItemDao.deleteAll(cartItems);
    }
}
