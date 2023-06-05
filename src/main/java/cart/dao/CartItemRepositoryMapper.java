package cart.dao;

import cart.domain.CartItem;
import cart.domain.CartItemEntity;
import cart.domain.Member;
import cart.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CartItemRepositoryMapper implements CartItemRepository {

    private final CartItemDao cartItemDao;
    private final ProductDao productDao;
    private final MemberDao memberDao;

    public CartItemRepositoryMapper(final CartItemDao cartItemDao, final ProductDao productDao, final MemberDao memberDao) {
        this.cartItemDao = cartItemDao;
        this.productDao = productDao;
        this.memberDao = memberDao;
    }

    @Override
    public List<CartItem> findByIds(final List<Long> ids) {
        List<CartItem> cartItems = new ArrayList<>();
        List<CartItemEntity> cartItemEntities = cartItemDao.findByIds(ids);
        for (final CartItemEntity cartItemEntity : cartItemEntities) {
            Product product = productDao.getProductById(cartItemEntity.getProductId());
            Member member = memberDao.getMemberById(cartItemEntity.getMemberId());
            cartItems.add(new CartItem(cartItemEntity.getId(), cartItemEntity.getQuantity(), product, member));
        }
        return cartItems;
    }

    @Override
    public void batchDelete(final Long memberId, final List<CartItem> removalCartItems) {
        cartItemDao.batchDelete(memberId, removalCartItems);
    }

}
