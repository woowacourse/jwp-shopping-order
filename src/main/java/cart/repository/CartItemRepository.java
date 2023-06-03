package cart.repository;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Product;
import cart.entity.CartItemEntity;
import cart.entity.ProductEntity;
import cart.exception.ProductNotFound;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemRepository {

    private final CartItemDao cartItemDao;
    private final ProductDao productDao;

    public CartItemRepository(CartItemDao cartItemDao, ProductDao productDao) {
        this.cartItemDao = cartItemDao;
        this.productDao = productDao;
    }

    public Long save(CartItem cartItem) {
        return cartItemDao.save(CartItemEntity.from(cartItem));
    }

    public List<CartItem> findAllByMemberId(Long memberId) {
        List<CartItemEntity> cartItemEntities = cartItemDao.findByMemberId(memberId);
        List<Long> productIds = cartItemEntities.stream()
                .map(CartItemEntity::getProductId)
                .collect(toList());
        Map<Long, Product> products = productDao.findAllByIds(productIds).stream()
                .map(ProductEntity::toDomain)
                .collect(toMap(Product::getId, Function.identity()));
        return cartItemEntities.stream()
                .map(it -> new CartItem(it.getId(), it.getQuantity(),  products.get(it.getProductId()), memberId))
                .collect(toList());
    }

    public Optional<CartItem> findById(Long id) {
        Optional<CartItemEntity> nullableCartItemEntity = cartItemDao.findById(id);
        if (nullableCartItemEntity.isEmpty()) {
            return Optional.empty();
        }
        CartItemEntity cartItemEntity = nullableCartItemEntity.get();
        Product product = productDao.findById(cartItemEntity.getId())
                .orElseThrow(ProductNotFound::new)
                .toDomain();
        return Optional.of(new CartItem(
                cartItemEntity.getId(),
                cartItemEntity.getQuantity(),
                product,
                cartItemEntity.getMemberId()));
    }

    public void deleteById(Long id) {
        cartItemDao.deleteById(id);
    }

    public void updateQuantity(CartItem cartItem) {
        cartItemDao.updateQuantity(CartItemEntity.from(cartItem));
    }

    public void clear(List<CartItem> cartItems) {
        List<Long> ids = cartItems.stream()
                .map(CartItem::getId)
                .collect(toList());
        cartItemDao.deleteByIds(ids);
    }
}
