package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.entity.CartItemEntity;
import cart.entity.MemberEntity;
import cart.entity.ProductEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class MysqlCartItemRepository implements CartItemRepository {

    private final CartItemDao cartItemDao;
    private final ProductDao productDao;
    private final MemberDao memberDao;

    public MysqlCartItemRepository(final CartItemDao cartItemDao, final ProductDao productDao, final MemberDao memberDao) {
        this.cartItemDao = cartItemDao;
        this.productDao = productDao;
        this.memberDao = memberDao;
    }

    @Override
    public CartItem findById(final long id) {
        final CartItemEntity cartItemEntity = cartItemDao.findById(id);
        return toCartItem(cartItemEntity);
    }

    private CartItem toCartItem(final CartItemEntity cartItemEntity) {
        final ProductEntity productEntity = productDao.findById(cartItemEntity.getProductId());
        final MemberEntity memberEntity = memberDao.findById(cartItemEntity.getMemberId());
        return new CartItem(cartItemEntity.getId(), cartItemEntity.getQuantity(), toProduct(productEntity), toMember(memberEntity));
    }

    private Product toProduct(final ProductEntity productEntity) {
        return new Product(productEntity.getId(), productEntity.getName(), productEntity.getPrice(),
                productEntity.getImageUrl());
    }

    private Member toMember(final MemberEntity memberEntity) {
        return new Member(memberEntity.getId(), memberEntity.getGrade(), memberEntity.getEmail(),
                memberEntity.getPassword());
    }

    @Override
    public List<CartItem> findByMemberId(final long memberId) {
        final List<CartItemEntity> cartItemEntities = cartItemDao.findByMemberId(memberId);
        return cartItemEntities.stream()
                .map(this::toCartItem)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public CartItem save(final CartItem cartItem) {
        final CartItemEntity cartItemEntity = toCartItemEntity(cartItem);
        final long id = cartItemDao.save(cartItemEntity);
        return new CartItem(id, cartItem.getQuantity(), cartItem.getProduct(), cartItem.getMember());
    }

    @Override
    public void deleteById(final long id) {
        cartItemDao.deleteById(id);
    }

    @Override
    public void updateQuantity(final CartItem cartItem) {
        final CartItemEntity cartItemEntity = toCartItemEntity(cartItem);
        cartItemDao.updateQuantity(cartItemEntity);
    }

    private CartItemEntity toCartItemEntity(final CartItem cartItem) {
        return new CartItemEntity(cartItem.getId(), cartItem.getMember().getId(), cartItem.getProduct().getId(),
                cartItem.getQuantity());
    }
}
