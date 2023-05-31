package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.CartItemEntity;
import cart.domain.Member;
import cart.domain.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class CartItemRepository {

    private final MemberDao memberDao;
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public void add(final CartItem cartItem) {
        CartItemEntity cartItemEntity = new CartItemEntity(cartItem.getMember().getId(), cartItem.getProduct().getId(), cartItem.getQuantity());

        cartItemDao.insert(cartItemEntity);
    }

    public CartItem findById(final Long id) {
        CartItemEntity foundCartItemEntity = cartItemDao.findById(id);

        Member member = memberDao.findById(foundCartItemEntity.getMemberId());
        Product product = productDao.findById(foundCartItemEntity.getProductId());
        int quantity = foundCartItemEntity.getQuantity();

        return new CartItem(id, member, product, quantity);
    }
}
