package cart.repository;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.cart.CartItem;
import cart.domain.cart.Product;
import cart.entity.CartItemEntity;
import cart.entity.ProductEntity;
import cart.exception.cart.ProductNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemRepository {

    private final CartItemDao cartItemDao;
    private final ProductDao productDao;

    public CartItemRepository(final CartItemDao cartItemDao, final ProductDao productDao) {
        this.cartItemDao = cartItemDao;
        this.productDao = productDao;
    }

    public CartItem save(final CartItem cartItem) {
        final CartItemEntity cartItemEntity = new CartItemEntity(
                cartItem.getId(),
                cartItem.getMemberId(),
                cartItem.getProduct().getId(),
                cartItem.getQuantity()
        );
        if (Objects.isNull(cartItem.getId())) {
            final CartItemEntity entity = cartItemDao.insert(cartItemEntity);
            return new CartItem(entity.getId(), cartItem.getQuantity(), cartItem.getMemberId(), cartItem.getProduct());
        }
        cartItemDao.updateQuantity(cartItemEntity);
        return cartItem;
    }

    public List<CartItem> findAllByMemberId(final Long memberId) {
        final List<CartItemEntity> cartItemEntities = cartItemDao.findAllByMemberId(memberId);

        final List<Long> productIds = cartItemEntities.stream()
                .map(CartItemEntity::getProductId)
                .collect(toList());
        final Map<Long, Product> products = productDao.findByIds(productIds).stream()
                .map(ProductEntity::toDomain)
                .collect(toMap(Product::getId, Function.identity()));

        return cartItemEntities.stream()
                .map(it -> new CartItem(it.getId(), it.getQuantity(), memberId, products.get(it.getProductId())))
                .collect(toList());
    }

    public Optional<CartItem> findById(Long id) {
        final Optional<CartItemEntity> mayBeCartItemEntity = cartItemDao.findById(id);
        if (mayBeCartItemEntity.isEmpty()) {
            return Optional.empty();
        }

        final CartItemEntity cartItemEntity = mayBeCartItemEntity.get();
        final Product product = productDao.findById(cartItemEntity.getProductId())
                .orElseThrow(ProductNotFoundException::new)
                .toDomain();
        
        return Optional.of(new CartItem(
                cartItemEntity.getId(),
                cartItemEntity.getQuantity(),
                cartItemEntity.getMemberId(),
                product
        ));
    }

    public void deleteById(final Long cartItemId) {
        cartItemDao.deleteById(cartItemId);
    }

    public void deleteByIds(final List<Long> cartItemIds) {
        cartItemDao.deleteByIds(cartItemIds);
    }
}
