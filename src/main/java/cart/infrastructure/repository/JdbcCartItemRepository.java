package cart.infrastructure.repository;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.domain.repository.CartItemRepository;
import cart.entity.CartItemEntity;
import cart.infrastructure.dao.CartItemDao;
import cart.infrastructure.dao.MemberDao;
import cart.infrastructure.dao.ProductDao;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcCartItemRepository implements CartItemRepository {

    private final CartItemDao cartItemDao;
    private final ProductDao productDao;
    private final MemberDao memberDao;

    public JdbcCartItemRepository(final CartItemDao cartItemDao, final ProductDao productDao,
                                  final MemberDao memberDao) {
        this.cartItemDao = cartItemDao;
        this.productDao = productDao;
        this.memberDao = memberDao;
    }

    @Override
    public Long create(final CartItem cartItem) {
        return cartItemDao.create(CartItemEntity.from(cartItem));
    }

    @Override
    public CartItem findById(final Long id) {
        final CartItemEntity cartItemEntity = cartItemDao.findById(id)
                .orElseThrow(NoSuchElementException::new);
        final Product product = findProduct(cartItemEntity.getProductId());
        final Member member = findMember(cartItemEntity.getMemberId());
        return new CartItem(cartItemEntity.getId(), cartItemEntity.getQuantity(), product, member);
    }

    private Member findMember(final Long id) {
        return memberDao.findById(id)
                .orElseThrow(NoSuchElementException::new)
                .toDomain();
    }

    private Product findProduct(final Long id) {
        return productDao.findById(id)
                .orElseThrow(NoSuchElementException::new)
                .toDomain();
    }

    @Override
    public Optional<CartItem> findByMemberIdAndProductId(final Long memberId, final Long productId) {
        final Optional<CartItemEntity> cartItemOptional = cartItemDao.findByMemberIdAndProductId(memberId, productId);
        if (cartItemOptional.isEmpty()) {
            return Optional.empty();
        }
        final CartItemEntity cartItemEntity = cartItemOptional.get();
        final Product product = findProduct(productId);
        final Member member = findMember(memberId);
        return Optional.of(new CartItem(cartItemEntity.getId(), cartItemEntity.getQuantity(), product, member));
    }

    @Override
    public List<CartItem> findByMemberId(final Long memberId) {
        final List<CartItemEntity> cartItemEntities = cartItemDao.findByMemberId(memberId);
        return cartItemEntities.stream()
                .map(it -> new CartItem(it.getId(), it.getQuantity(), findProduct(it.getProductId()),
                        findMember(it.getMemberId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<CartItem> findAllByIds(final List<Long> ids) {
        final List<CartItemEntity> cartItemEntities = cartItemDao.findAllByIds(ids).stream()
                .map(it -> it.orElseThrow(NoSuchElementException::new))
                .collect(Collectors.toList());
        return cartItemEntities.stream()
                .map(it -> new CartItem(it.getId(), it.getQuantity(), findProduct(it.getProductId()),
                        findMember(it.getMemberId())))
                .collect(Collectors.toList());
    }

    @Override
    public void updateQuantity(final CartItem cartItem) {
        cartItemDao.updateQuantity(CartItemEntity.from(cartItem));
    }

    @Override
    public void deleteById(final Long id) {
        cartItemDao.deleteById(id);
    }

    @Override
    public void deleteByMemberIdAndProductId(final Long memberId, final Long productId) {
        cartItemDao.deleteByMemberIdAndProductId(memberId, productId);
    }

    @Override
    public void deleteAll(final List<CartItem> cartItems) {
        final List<CartItemEntity> cartItemEntities = cartItems.stream()
                .map(CartItemEntity::from)
                .collect(Collectors.toList());
        cartItemDao.deleteAll(cartItemEntities);
    }
}
