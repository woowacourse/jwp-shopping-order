package cart.application;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.dto.RemoveCartItemsRequest;
import cart.exception.BusinessException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public CartItemService(ProductDao productDao, CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        return cartItems.stream().map(CartItemResponse::of).collect(Collectors.toList());
    }

    public Long add(Member member, CartItemRequest cartItemRequest) {
        final Optional<CartItem> optionalCartItem = cartItemDao.findByMemberIdAndProductId(member.getId(),
            cartItemRequest.getProductId());

        return optionalCartItem
            .map(cartItem -> {
                updateQuantity(cartItem, cartItemRequest.getQuantity());
                return cartItem.getId();
            })
            .orElseGet(() -> {
                final Product product = productDao.getProductById(cartItemRequest.getProductId())
                    .orElseThrow(() -> new BusinessException("찾는 상품이 존재하지 않습니다."));
                final CartItem cartItem = new CartItem(member, product);
                return cartItemDao.save(cartItem);
            });
    }

    private void updateQuantity(CartItem cartItem, int quantity) {
        cartItem.changeQuantity(quantity);
        cartItemDao.updateQuantity(cartItem);
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemDao.deleteById(id);
            return;
        }

        updateQuantity(cartItem, request.getQuantity());
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        cartItemDao.deleteById(id);
    }

    public void remove(final Member member, final RemoveCartItemsRequest request) {
        final List<CartItem> cartItems = cartItemDao.findAllByIds(request.getCartItemIds());
        for (final CartItem cartItem : cartItems) {
            cartItem.checkOwner(member);
        }
        cartItemDao.deleteAll(cartItems);
    }
}
