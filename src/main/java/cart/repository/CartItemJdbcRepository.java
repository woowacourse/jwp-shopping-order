package cart.repository;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Price;
import cart.domain.Product;
import cart.domain.Quantity;
import cart.entity.CartItemWithMemberAndProductEntity;
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
        List<CartItemWithProductEntity> entities = cartItemDao.findProductDetailByIds(ids);
        return null;
    }

    @Override
    public List<CartItem> findByMember(final Member member) {
        List<CartItemWithMemberAndProductEntity> entities = cartItemDao.findAllDetailByMemberId(member.getId());
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

    private List<CartItem> mapToDomains(final List<CartItemWithMemberAndProductEntity> entities) {
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toUnmodifiableList());
    }

    private CartItem toDomain(final CartItemWithMemberAndProductEntity entity) {
        return new CartItem(
                entity.getId(),
                new Member(entity.getMemberId(), entity.getMemberEmail()),
                new Product(entity.getProductId(), entity.getProductName(),
                        new Price(entity.getProductPrice()), entity.getProductImageUrl()),
                new Quantity(entity.getQuantity())
        );
    }
}
