package cart.db.repository;

import cart.db.dao.CartItemDao;
import cart.db.entity.CartItemDetailEntity;
import cart.db.entity.CartItemEntity;
import cart.domain.cart.CartItem;
import org.springframework.stereotype.Repository;

import java.util.List;

import static cart.db.mapper.CartItemMapper.toDomain;
import static cart.db.mapper.CartItemMapper.toEntity;

@Repository
public class CartItemRepository {

    private final CartItemDao cartItemDao;

    public CartItemRepository(final CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    public Long save(final CartItem cartItem) {
        CartItemEntity cartItemEntity = toEntity(cartItem);
        return cartItemDao.create(cartItemEntity);
    }

    public List<CartItem> findByMemberId(final Long memberId) {
        List<CartItemDetailEntity> cartItemDetailEntities = cartItemDao.findByMemberId(memberId);
        return toDomain(cartItemDetailEntities);
    }

    public CartItem findById(final Long id) {
        List<CartItemDetailEntity> cartItemDetailEntities = cartItemDao.findById(id);
        if (cartItemDetailEntities.isEmpty()) {
            return null;
        }
        return toDomain(cartItemDetailEntities.get(0));
    }

    public Long countByIdsAndMemberId(final Long memberId, List<Long> ids) {
        return cartItemDao.countByIdsAndMemberId(memberId, ids);
    }

    public void updateQuantity(final CartItem cartItem) {
        CartItemEntity cartItemEntity = toEntity(cartItem);
        cartItemDao.updateQuantity(cartItemEntity);
    }

    public void deleteById(final Long id) {
        cartItemDao.deleteById(id);
    }

    public void deleteByIds(final Long memberId, final List<Long> ids) {
        cartItemDao.deleteByIdsAndMemberId(memberId, ids);
    }
}
