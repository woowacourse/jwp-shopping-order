package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.entity.CartItemEntity;
import cart.domain.CartItem;
import cart.domain.Product;
import cart.exception.CartItemException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemRepository {

    private final CartItemDao cartItemDao;
    private final ProductRepository productRepository;

    public CartItemRepository(final CartItemDao cartItemDao, final ProductRepository productRepository) {
        this.cartItemDao = cartItemDao;
        this.productRepository = productRepository;
    }

    public long add(final long memberId, final CartItem cartItem) {
        final long productId = cartItem.getProduct().getId();
        validateDuplicate(memberId, productId);
        return cartItemDao.save(new CartItemEntity(memberId, productId, cartItem.getQuantity()));
    }

    private void validateDuplicate(final long memberId, final long productId) {
        if (cartItemDao.isExist(memberId, productId)) {
            throw new CartItemException.ExistingProductId(productId);
        }
    }

    public List<CartItem> findByMember(final long memberId) {
        final List<CartItemEntity> foundCartItems = cartItemDao.findByMemberId(memberId);
        return foundCartItems.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    public Optional<CartItem> findByIdForMember(final long memberId, final long id) {
        return cartItemDao.findByIdForMember(memberId, id)
                .map(this::convert);
    }

    private CartItem convert(final CartItemEntity foundCartItem) {
        final Product foundProduct = productRepository.findById(foundCartItem.getProductId())
                .orElseThrow(() -> new IllegalStateException("illegal data exists in table CART_ITEM; product_id"));
        return foundCartItem.create(foundProduct);
    }

    public void update(final CartItem cartItem) {
        cartItemDao.updateQuantity(cartItem);
    }

    public void removeById(final long id) {
        cartItemDao.deleteById(id);
    }

    public void removeAllById(final List<Long> ids) {
        cartItemDao.deleteByIds(ids);
    }
}

