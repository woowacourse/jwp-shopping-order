package cart.application;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.cartItem.CartItem;
import cart.domain.member.Member;
import cart.dto.cartItem.CartItemQuantityUpdateRequest;
import cart.dto.cartItem.CartItemRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CartItemService {
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public CartItemService(ProductDao productDao, CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    public List<CartItem> findByMember(Member member) {
        return cartItemDao.findByMemberId(member.getId());
    }

    @Transactional
    public Long add(Member member, CartItemRequest cartItemRequest) {
        return cartItemDao.save(CartItem.createInitial(member, productDao.getProductById(cartItemRequest.getId())));
    }

    @Transactional
    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemDao.deleteById(id);
            return;
        }

        CartItem updateCartItem = cartItem.changeQuantity(request.getQuantity());
        cartItemDao.updateQuantity(updateCartItem);
    }

    @Transactional
    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        cartItemDao.deleteById(id);
    }
}
