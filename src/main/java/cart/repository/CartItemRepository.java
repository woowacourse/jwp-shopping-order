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
import java.util.List;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class CartItemRepository {

    private final MemberDao memberDao;
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public Long add(final CartItem cartItem) {
        CartItemEntity cartItemEntity = new CartItemEntity(cartItem.getMember().getId(), cartItem.getProduct().getId(), cartItem.getQuantity());

        return cartItemDao.insert(cartItemEntity);
    }

    public CartItem findById(final Long cartItemId) {
        CartItemEntity foundCartItemEntity = cartItemDao.findById(cartItemId);

        return toDomain(foundCartItemEntity);
    }

    private CartItem toDomain(final CartItemEntity cartItemEntity) {
        Member member = memberDao.findById(cartItemEntity.getMemberId());
        Product product = productDao.findById(cartItemEntity.getProductId());
        int quantity = cartItemEntity.getQuantity();

        return new CartItem(cartItemEntity.getId(), member, product, quantity);
    }

    public List<CartItem> findByMemberId(final Long memberId) {
        List<CartItemEntity> cartItemEntities = cartItemDao.findByMemberId(memberId);

        return cartItemEntities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
}
