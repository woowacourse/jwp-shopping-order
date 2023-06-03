package cart.repository;

import cart.dao.CartItemDao2;
import cart.dao.MemberDao2;
import cart.dao.ProductDao2;
import cart.dao.entity.CartItemEntity;
import cart.dao.entity.MemberEntity;
import cart.dao.entity.ProductEntity;
import cart.domain.CartItem;
import cart.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class CartItemRepository {
    private final CartItemDao2 cartItemDao;
    private final ProductDao2 productDao;
    private final MemberDao2 memberDao2;

    public CartItemRepository(final CartItemDao2 cartItemDao, final ProductDao2 productDao, final MemberDao2 memberDao2) {
        this.cartItemDao = cartItemDao;
        this.productDao = productDao;
        this.memberDao2 = memberDao2;
    }

    public List<CartItem> findByMember(final Member member) {
        Map<Long, ProductEntity> allProductsById = productDao.findAll().stream()
                .collect(Collectors.toMap(ProductEntity::getId, productEntity -> productEntity));

        List<CartItemEntity> cartItemEntities = cartItemDao.findByMemberId(member.getId());

        return cartItemEntities.stream()
                .map(cartItemEntity -> cartItemEntity.toCartItem(allProductsById, member))
                .collect(Collectors.toList());
    }

    public List<CartItem> findByIds(final List<Long> cartItemsIds) {
        List<CartItemEntity> cartItems = cartItemDao.findByIds(cartItemsIds);
        Map<Long, MemberEntity> allMembersById = memberDao2.findAll().stream()
                .collect(Collectors.toMap(MemberEntity::getId, memberEntity -> memberEntity));
        Map<Long, ProductEntity> allProductsById = productDao.findAll().stream()
                .collect(Collectors.toMap(ProductEntity::getId, productEntity -> productEntity));
        return cartItems.stream()
                .map(cartItemEntity -> {
                    Member member = allMembersById.get(cartItemEntity.getMemberId()).toMember();
                    return cartItemEntity.toCartItem(allProductsById, member);
                })
                .collect(Collectors.toList());
    }

    public void deleteAll(final List<CartItem> cartItems) {
        List<Long> ids = cartItems.stream()
                .map(CartItem::getId)
                .collect(Collectors.toList());
        cartItemDao.deleteAllByIds(ids);
    }
}
