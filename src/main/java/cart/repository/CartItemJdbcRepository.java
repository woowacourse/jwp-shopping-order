package cart.repository;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Price;
import cart.domain.Product;
import cart.domain.Quantity;
import cart.entity.CartItemEntity;
import cart.entity.CartItemWithProductEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CartItemJdbcRepository implements CartItemRepository {

    private final CartItemDao cartItemDao;

    public CartItemJdbcRepository(final CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    @Override
    public List<CartItem> findByIds(final List<Long> ids) {
        final List<CartItemWithProductEntity> entities = cartItemDao.findProductDetailByIds(ids);
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<CartItem> findByMember(final Member member) {
        final List<CartItemWithProductEntity> entities = cartItemDao.findAllDetailByMemberId(member.getId());
        return mapToDomains(entities);
    }

    @Override
    public long save(final Member member, final long productId, final int quantity) {
        final CartItemEntity entity = new CartItemEntity(member.getId(), productId, quantity);
        return cartItemDao.save(entity);
    }

    @Override
    public void updateQuantity(final long id, final int quantity) {
        cartItemDao.updateQuantity(id, quantity);
    }

    @Override
    public void delete(final long id) {
        cartItemDao.deleteById(id);
    }

    @Override
    public long findMemberIdById(final long id) {
        return cartItemDao.findMemberIdById(id);
    }

    @Override
    public void deleteAll(final List<Long> ids) {
        cartItemDao.deleteAllByIds(ids);
    }

    private List<CartItem> mapToDomains(final List<CartItemWithProductEntity> entities) {
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toUnmodifiableList());
    }

    private CartItem toDomain(final CartItemWithProductEntity entity) {
        return new CartItem(
                entity.getId(),
                entity.getMemberId(),
                new Product(entity.getProductId(), entity.getProductName(),
                        new Price(entity.getProductPrice()), entity.getProductImageUrl()),
                new Quantity(entity.getQuantity())
        );
    }
}
