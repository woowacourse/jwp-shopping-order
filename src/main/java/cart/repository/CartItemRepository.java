package cart.repository;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.entity.CartItemEntity;
import cart.entity.ProductEntity;
import cart.exception.MemberNotFoundException;
import cart.exception.ProductNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
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

    public CartItem save(final CartItem cartItem) {
        final CartItemEntity cartItemEntity = new CartItemEntity(
                cartItem.getId(),
                cartItem.getMember().getId(),
                cartItem.getProduct().getId(),
                cartItem.getQuantity()
        );
        if (Objects.isNull(cartItem.getId())) {
            final CartItemEntity entity = cartItemDao.insert(cartItemEntity);
            return new CartItem(entity.getId(), cartItem.getQuantity(), cartItem.getMember(), cartItem.getProduct());
        }
        cartItemDao.updateQuantity(cartItemEntity);
        return cartItem;
    }

    public List<CartItem> findAllByMemberId(final Long memberId) {
        final List<CartItemEntity> cartItemEntities = cartItemDao.findAllByMemberId(memberId);
        final Member member = memberDao.findById(memberId)
                .orElseThrow(MemberNotFoundException::new)
                .toDomain();
        final List<Long> productIds = cartItemEntities.stream()
                .map(CartItemEntity::getProductId)
                .collect(toList());
        final Map<Long, Product> products = productDao.findByIds(productIds).stream()
                .map(ProductEntity::toDomain)
                .collect(toMap(Product::getId, Function.identity()));
        return cartItemEntities.stream()
                .map(it -> new CartItem(it.getId(), it.getQuantity(), member, products.get(it.getProductId())))
                .collect(toList());
    }

    public Optional<CartItem> findById(Long id) {
        final Optional<CartItemEntity> mayBeCartItemEntity = cartItemDao.findById(id);
        if (mayBeCartItemEntity.isEmpty()) {
            return Optional.empty();
        }
        final CartItemEntity cartItemEntity = mayBeCartItemEntity.get();
        final Member member = memberDao.findById(cartItemEntity.getMemberId())
                .orElseThrow(MemberNotFoundException::new)
                .toDomain();
        final Product product = productDao.findById(cartItemEntity.getProductId())
                .orElseThrow(ProductNotFoundException::new)
                .toDomain();
        return Optional.of(new CartItem(cartItemEntity.getId(), cartItemEntity.getQuantity(), member, product));
    }

    public void deleteAll(final List<CartItem> cartItems) {
        final List<Long> ids = cartItems.stream()
                .map(CartItem::getId)
                .collect(toList());
        cartItemDao.deleteByIdIn(ids);
    }

    public void delete(final CartItem cartItem) {
        cartItemDao.deleteById(cartItem.getId());
    }
}
