package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Product;
import cart.entity.CartItemEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CartItemRepository {
    private final CartItemDao cartItemDao;
    private final ProductDao productDao;
    private final MemberDao memberDao;

    public CartItemRepository(final CartItemDao cartItemDao, final ProductDao productDao, final MemberDao memberDao) {
        this.cartItemDao = cartItemDao;
        this.productDao = productDao;
        this.memberDao = memberDao;
    }

    public long save(final CartItem cartItem) {
        final CartItemEntity entity = CartItemEntity.from(cartItem);
        return cartItemDao.save(entity);
    }

    public List<CartItem> findByMemberId(final Long memberId) {
        final List<CartItemEntity> entity = cartItemDao.findByMemberId(memberId);
        return entity.stream()
                .map(it -> new CartItem(
                        it.getId(),
                        it.getQuantity(),
                        productDao.getProductById(it.getProductId()).toProduct(),
                        memberDao.getMemberById(it.getMemberId())
                ))
                .collect(Collectors.toList());
    }

    public Product findProductOf(final Long id) {
        final CartItem cartItem = cartItemDao.findById(id);
        return cartItem.getProduct();
    }

    public int findQuantityOf(final Long id) {
        final CartItem cartItem = cartItemDao.findById(id);
        return cartItem.getQuantity();
    }
}
