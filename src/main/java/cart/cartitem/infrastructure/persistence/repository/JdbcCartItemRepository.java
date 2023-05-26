package cart.cartitem.infrastructure.persistence.repository;

import static cart.cartitem.exception.CartItemExceptionType.NOT_FOUND_CART_ITEM;

import cart.cartitem.domain.CartItem;
import cart.cartitem.domain.CartItemRepository;
import cart.cartitem.exception.CartItemException;
import cart.cartitem.infrastructure.persistence.dao.CartItemDao;
import cart.cartitem.infrastructure.persistence.entity.CartItemEntity;
import cart.cartitem.infrastructure.persistence.mapper.CartItemEntityMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcCartItemRepository implements CartItemRepository {

    private final CartItemDao cartItemDao;

    public JdbcCartItemRepository(CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    @Override
    public Long save(CartItem cartItem) {
        CartItemEntity entity = CartItemEntityMapper.toEntity(cartItem);
        return cartItemDao.save(entity);
    }

    @Override
    public void update(CartItem cartItem) {
        CartItemEntity entity = CartItemEntityMapper.toEntity(cartItem);
        cartItemDao.updateQuantity(entity);
    }

    @Override
    public void deleteById(Long id) {
        cartItemDao.deleteById(id);
    }

    @Override
    public CartItem findById(Long id) {
        CartItemEntity entity = cartItemDao.findById(id)
                .orElseThrow(() -> new CartItemException(NOT_FOUND_CART_ITEM));
        return CartItemEntityMapper.toDomain(entity);
    }

    @Override
    public List<CartItem> findByMemberId(Long memberId) {
        return cartItemDao.findByMemberId(memberId)
                .stream()
                .map(CartItemEntityMapper::toDomain)
                .collect(Collectors.toList());
    }
}
