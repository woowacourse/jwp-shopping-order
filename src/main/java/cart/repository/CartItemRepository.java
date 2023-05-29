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
        final Member member = getMember(memberId);
        final Map<Long, Product> products = getProducts(cartItemEntities);
        return makeCartItems(cartItemEntities, member, products);
    }

    public Optional<CartItem> findById(Long id) {
        final Optional<CartItemEntity> mayBeCartItemEntity = cartItemDao.findById(id);
        if (mayBeCartItemEntity.isEmpty()) {
            return Optional.empty();
        }
        final CartItemEntity cartItemEntity = mayBeCartItemEntity.get();
        final Member member = getMember(cartItemEntity.getMemberId());
        final Product product = getProduct(cartItemEntity);
        return Optional.of(new CartItem(cartItemEntity.getId(), cartItemEntity.getQuantity(), member, product));
    }

    private Product getProduct(final CartItemEntity cartItemEntity) {
        return productDao.findById(cartItemEntity.getProductId())
                .orElseThrow(ProductNotFoundException::new)
                .toDomain();
    }

    public void deleteById(final Long cartItemId, final Long memberId) {
        cartItemDao.deleteById(cartItemId, memberId);
    }

    private List<CartItem> makeCartItems(final List<CartItemEntity> cartItemEntities, final Member member,
                                             final Map<Long, Product> products) {
        return cartItemEntities.stream()
                .map(it -> new CartItem(it.getId(), it.getQuantity(), member, products.get(it.getProductId())))
                .collect(toList());
    }

    private Map<Long, Product> getProducts(final List<CartItemEntity> cartItemEntities) {
        final List<Long> productIds = cartItemEntities.stream()
                .map(CartItemEntity::getProductId)
                .collect(toList());
        return productDao.findByIds(productIds).stream()
                .map(ProductEntity::toDomain)
                .collect(toMap(Product::getId, Function.identity()));
    }

    private Member getMember(final Long memberId) {
        return memberDao.findById(memberId)
                .orElseThrow(MemberNotFoundException::new)
                .toDomain();
    }

    public List<CartItem> findAllByCartItemIds(final Long memberId, final List<Long> orderItemIds) {
        final List<CartItemEntity> cartItemEntities = cartItemDao.findAllByCartItemIds(orderItemIds);
        final Member member = getMember(memberId);
        final Map<Long, Product> products = getProducts(cartItemEntities);
        return makeCartItems(cartItemEntities, member, products);
    }
}
