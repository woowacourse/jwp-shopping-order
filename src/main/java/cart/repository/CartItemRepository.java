package cart.repository;

import cart.domain.Member;
import cart.domain.Product;
import cart.domain.cart.CartItem;
import cart.entity.CartItemEntity;
import cart.entity.ProductEntity;
import cart.repository.dao.CartItemDao;
import cart.repository.dao.MemberDao;
import cart.repository.dao.ProductDao;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemRepository {

    private final CartItemDao cartItemDao;
    private final MemberDao memberDao;
    private final ProductDao productDao;

    public CartItemRepository(final CartItemDao cartItemDao, final MemberDao memberDao, final ProductDao productDao) {
        this.cartItemDao = cartItemDao;
        this.memberDao = memberDao;
        this.productDao = productDao;
    }

    public List<CartItem> findByMemberId(Long memberId) {
        final List<CartItemEntity> findCartItems = cartItemDao.findByMemberId(memberId);

        return findCartItems.stream()
                .map(this::mapToDomain)
                .collect(Collectors.toUnmodifiableList());
    }

    private CartItem mapToDomain(CartItemEntity cartItemEntity) {
        final Member member = memberDao.getMemberById(cartItemEntity.getMemberId());
        final ProductEntity productEntity = productDao.getProductById(cartItemEntity.getProductId());

        return new CartItem(cartItemEntity.getId(), cartItemEntity.getQuantity(), Product.from(productEntity), member);
    }

    public List<CartItem> findByIds(List<Long> ids) {
        final List<CartItemEntity> findCartItems = cartItemDao.findByIds(ids);

        return findCartItems.stream()
                .map(this::mapToDomain)
                .collect(Collectors.toUnmodifiableList());
    }

    public Optional<CartItem> findByMemberIdAndProductId(Long memberId, Long productId) {
        final List<CartItemEntity> cartItems = cartItemDao.findByMemberId(memberId);

        return cartItems.stream()
                .filter(cartItemEntity -> cartItemEntity.getProductId().equals(productId))
                .findFirst()
                .map(this::mapToDomain);
    }

    public void updateQuantity(CartItem cartItem) {
        final CartItemEntity updateCartItem = CartItemEntity.from(cartItem);
        cartItemDao.updateQuantity(updateCartItem);
    }

    public Long save(final CartItem cartItem) {
        final CartItemEntity saveCartItem = CartItemEntity.from(cartItem);
        return cartItemDao.save(saveCartItem);
    }

    public Optional<CartItem> findById(Long cartItemId) {
        final Optional<CartItemEntity> findCartItem = cartItemDao.findById(cartItemId);
        return findCartItem.map(this::mapToDomain);
    }

    public void deleteById(Long cartItemId) {
        cartItemDao.deleteById(cartItemId);
    }

    public Page<CartItem> getPagedCartItemsByMember(final Pageable pageable, final Long memberId) {
        final Page<CartItemEntity> cartItemEntities = cartItemDao.getCartItemsByMemberId(pageable, memberId);

        return cartItemEntities.map(cartItemEntity -> mapToDomain(cartItemEntity));
    }

    public void deleteByIds(final List<Long> cartItemIds) {
        cartItemDao.deleteByIds(cartItemIds);
    }
}
