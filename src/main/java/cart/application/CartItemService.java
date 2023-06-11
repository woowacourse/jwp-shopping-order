package cart.application;

import java.util.List;

import org.springframework.stereotype.Service;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;

@Service
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

    public CartItem getItemBy(Long id) {
        return cartItemDao.findById(id);
    }

    public CartItem getItemByProductId(Long productId) {
        return cartItemDao.findByProductId(productId);
    }

    public Long add(Member member, CartItemRequest cartItemRequest) {
        Product product = productDao.getProductById(cartItemRequest.getProductId());
        return cartItemDao.save(new CartItem(member, product));
    }

    public void add(List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            cartItemDao.save(cartItem);
        }
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemDao.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemDao.updateQuantity(cartItem);
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        cartItemDao.deleteById(id);
    }

    public void removeAllOf(Member member) {
        cartItemDao.deleteAllBy(member.getId());
    }
}
