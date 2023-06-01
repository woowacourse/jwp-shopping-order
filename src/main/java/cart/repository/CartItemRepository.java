package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.dao.entity.CartItemEntity;
import cart.dao.entity.ProductEntity;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.exception.CartItemException;
import cart.exception.ProductException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemRepository {

    private final CartItemDao cartItemDao;
    private final ProductDao productDao;

    public CartItemRepository(final CartItemDao cartItemDao, final ProductDao productDao) {
        this.cartItemDao = cartItemDao;
        this.productDao = productDao;
    }

    public long add(final CartItem cartItem) {
        final Long memberId = cartItem.getMember().getId();
        final Long productId = cartItem.getProduct().getId();
        validateDuplicate(memberId, productId);
        return cartItemDao.save(new CartItemEntity(memberId, productId, cartItem.getQuantity()));
    }

    public CartItem create(final Member member, final Long productId) {
        final ProductEntity product = productDao.findById(productId)
                .orElseThrow(() -> new ProductException.IllegalId(productId));
        return new CartItem(member, Product.from(product));
    }

    private void validateDuplicate(final Long memberId, final Long productId) {
        if (cartItemDao.isExist(memberId, productId)) {
            throw new CartItemException.ExistingProductId(productId);
        }
    }

    public List<CartItem> findByMember(final Member member) {
        final List<CartItemEntity> foundCartItems = cartItemDao.findByMemberId(member.getId());
        return foundCartItems.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    public Optional<CartItem> findById(final Long id) {
        final Optional<CartItemEntity> foundCartItem = cartItemDao.findById(id);
        if (foundCartItem.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(convert(foundCartItem.get()));
    }

    private CartItem convert(final CartItemEntity foundCartItem) {
        final ProductEntity foundProduct = productDao.findById(foundCartItem.getProductId())
                .orElseThrow(() -> new IllegalStateException("illegal data exists in table CART_ITEM; product_id"));
        return new CartItem(foundCartItem.getId(), new Member(foundCartItem.getMemberId()),
                Product.from(foundProduct),
                foundCartItem.getQuantity()
        );
    }

    public void update(final CartItem cartItem) {
        cartItemDao.updateQuantity(cartItem);
    }

    public void removeById(final Long id) {
        cartItemDao.deleteById(id);
    }

    public void removeAllById(final List<Long> ids) {
        cartItemDao.deleteByIds(ids);
    }
}

