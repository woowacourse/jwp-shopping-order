package cart.dao;

import cart.domain.CartItem;
import cart.domain.CartItemEntity;
import cart.domain.Member;
import cart.domain.MemberEntity;
import cart.domain.Product;
import cart.domain.ProductEntity;
import cart.exception.MemberIdNotMatchedException;
import cart.exception.ProductIdNotMatchedException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        final List<CartItemEntity> cartItemEntities = cartItemDao.findByIds(ids);
        final List<ProductEntity> products = productDao.findByIds(getProductIds(cartItemEntities));
        final List<MemberEntity> members = memberDao.findByIds(getMemberIds(cartItemEntities));

        for (final CartItemEntity cartItemEntity : cartItemEntities) {
            Product product = getProductIfMatchId(products, cartItemEntity);
            Member member = getMemberIfMatchId(members, cartItemEntity);
            cartItems.add(new CartItem(cartItemEntity.getId(), cartItemEntity.getQuantity(), product, member));
        }

        return cartItems;
    }

    private List<Long> getProductIds(final List<CartItemEntity> cartItemEntities) {
        return cartItemEntities.stream()
                .map(CartItemEntity::getId)
                .collect(Collectors.toList());
    }

    private List<Long> getMemberIds(final List<CartItemEntity> cartItemEntities) {
        return cartItemEntities.stream()
                .map(CartItemEntity::getMemberId)
                .collect(Collectors.toList());
    }

    private Product getProductIfMatchId(final List<ProductEntity> products, final CartItemEntity cartItemEntity) {
        for (final ProductEntity productEntity : products) {
            if (productEntity.getId() == cartItemEntity.getProductId()) {
                return Product.from(productEntity);
            }
        }
        throw ProductIdNotMatchedException.THROW;
    }

    private Member getMemberIfMatchId(final List<MemberEntity> memberEntities, final CartItemEntity cartItemEntity) {
        for (final MemberEntity memberEntity : memberEntities) {
            if (memberEntity.getId() == cartItemEntity.getMemberId()) {
                return Member.from(memberEntity);
            }
        }
        throw MemberIdNotMatchedException.THROW;
    }

    @Override
    public void batchDelete(final Long memberId, final List<CartItem> removalCartItems) {
        cartItemDao.batchDelete(memberId, removalCartItems);
    }

}
