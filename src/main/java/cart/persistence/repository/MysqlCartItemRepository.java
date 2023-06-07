package cart.persistence.repository;

import cart.application.repository.CartItemRepository;
import cart.persistence.dao.CartItemDao;
import cart.persistence.dao.MemberDao;
import cart.persistence.dao.ProductDao;
import cart.application.domain.CartItem;
import cart.application.domain.Member;
import cart.application.domain.Product;
import cart.persistence.entity.CartItemEntity;
import cart.persistence.entity.MemberEntity;
import cart.persistence.entity.ProductEntity;
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

    @Override
    public List<CartItem> findAllByIds(final List<Long> ids) {
        final List<CartItemEntity> cartItemEntities = cartItemDao.findAllByIds(ids);
        return cartItemEntities.stream()
                .map(this::toCartItem)
                .collect(Collectors.toList());
    }

    private CartItem toCartItem(final CartItemEntity cartItemEntity) {
        final ProductEntity productEntity = productDao.findById(cartItemEntity.getProductId());
        final MemberEntity memberEntity = memberDao.findById(cartItemEntity.getMemberId());
        final Product product = new Product(productEntity.getId(), productEntity.getName(), productEntity.getPrice(), productEntity.getImageUrl());
        final Member member = new Member(memberEntity.getId(), memberEntity.getGrade(), memberEntity.getEmail(), memberEntity.getPassword());
        return new CartItem(cartItemEntity.getId(), cartItemEntity.getQuantity(), product, member);
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
        final long id = cartItemDao.createCartItem(cartItemEntity);
        return new CartItem(id, cartItem.getQuantity(), cartItem.getProduct(), cartItem.getMember());
    }

    private CartItemEntity toCartItemEntity(final CartItem cartItem) {
        return new CartItemEntity(cartItem.getId(), cartItem.getMember().getId(), cartItem.getProduct().getId(),
                cartItem.getQuantity());
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
}
