package cart.repository;

import cart.domain.product.CartItem;
import cart.db.dao.CartItemDao;
import cart.db.entity.CartItemEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CartItemRepository {

    private final CartItemDao cartItemDao;

    public CartItemRepository(final CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    public List<CartItem> findByMemberId(Long memberId) {
        return cartItemDao.findByMemberId(memberId).stream()
                .map(CartItemEntity::toDomain)
                .collect(Collectors.toList());
    }

    public Long save(CartItem cartItem) {
        return cartItemDao.save(CartItemEntity.of(cartItem));
    }

    public CartItem findById(Long id) {
        return cartItemDao.findById(id).toDomain();
    }

    public List<CartItem> findAllByIds(List<Long> ids) {
        return cartItemDao.findAllByIds(ids).stream()
                .map(CartItemEntity::toDomain)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        cartItemDao.deleteById(id);
    }

    public void updateQuantity(CartItem cartItem) {
        cartItemDao.updateQuantity(CartItemEntity.of(cartItem));
    }
}

