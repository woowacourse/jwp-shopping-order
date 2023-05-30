package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.entity.CartItemEntity;
import cart.domain.cartitem.CartItem;
import cart.exception.CartItemException.NotFound;
import cart.repository.mapper.CartItemMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemRepository {

    private final CartItemDao cartItemDao;

    public CartItemRepository(CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    public List<CartItem> findByMemberId(Long memberId) {
        List<CartItemEntity> cartItemEntities = cartItemDao.findByMemberId(memberId);
        return cartItemEntities.stream()
                .map(CartItemMapper::toDomain)
                .collect(Collectors.toList());
    }

    public Optional<CartItem> findByMemberIdAndProductId(Long memberId, Long productId) {
        return cartItemDao.findByMemberIdAndProductId(memberId, productId)
                .map(CartItemMapper::toDomain);
    }

    public CartItem findById(Long id) {
        return cartItemDao.findById(id)
                .map(CartItemMapper::toDomain)
                .orElseThrow(NotFound::new);
    }

    public Long save(CartItem cartItem) {
        return cartItemDao.save(CartItemMapper.toEntity(cartItem));
    }

    public void updateQuantity(CartItem cartItem) {
        cartItemDao.updateQuantity(CartItemMapper.toEntity(cartItem));
    }

    public void deleteById(Long id) {
        cartItemDao.deleteById(id);
    }

    public void deleteAllByProductId(Long productId) {
        cartItemDao.deleteAllByProductId(productId);
    }
}
