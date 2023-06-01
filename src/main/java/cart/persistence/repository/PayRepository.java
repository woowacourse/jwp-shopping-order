package cart.persistence.repository;

import cart.domain.cart.CartItem;
import cart.domain.member.Member;
import cart.persistence.dao.CartItemDao;
import cart.persistence.dao.MemberDao;
import cart.persistence.dao.ProductDao;
import cart.persistence.entity.CartItemEntity;
import cart.persistence.entity.MemberEntity;
import cart.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cart.persistence.repository.Mapper.memberEntityMapper;

@Component
public class PayRepository {

    private final MemberDao memberDao;
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public PayRepository(final MemberDao memberDao, final ProductDao productDao, final CartItemDao cartItemDao) {
        this.memberDao = memberDao;
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    public void updatePoint(final Member member) {
        final MemberEntity memberEntity = memberEntityMapper(member);
        memberDao.updatePoint(memberEntity);
    }

    public List<CartItem> getCartItemsByIds(final List<Long> ids) {
        final List<CartItemEntity> cartItemEntities = cartItemDao.findByIds(ids);
        final List<Long> memberIds = cartItemEntities.stream()
                .map(CartItemEntity::getMemberId)
                .collect(Collectors.toList());
        final List<MemberEntity> memberEntities = memberDao.getMembersByIds(memberIds);
        final Map<Long, MemberEntity> memberEntityMap = memberEntities.stream()
                .collect(Collectors.toMap(MemberEntity::getId, Function.identity()));
        final List<ProductEntity> productEntities = getProductEntitiesFromCartItemEntities(cartItemEntities);

        final List<CartItem> cartItems = new ArrayList<>();
        for (int i = 0; i < cartItemEntities.size(); i++) {
            final CartItemEntity cartItemEntity = cartItemEntities.get(i);
            final MemberEntity memberEntity = memberEntityMap.get(cartItemEntity.getMemberId());
            final ProductEntity productEntity = productEntities.get(i);
            cartItems.add(Mapper.cartItemMapper(cartItemEntity, memberEntity, productEntity));
        }
        return cartItems;
    }

    private List<ProductEntity> getProductEntitiesFromCartItemEntities(final List<CartItemEntity> cartItemEntities) {
        final List<Long> productIds = cartItemEntities.stream()
                .map(CartItemEntity::getProductId)
                .collect(Collectors.toList());
        return productDao.getProductsByIds(productIds);
    }
}
