package cart.persistence.repository;

import cart.domain.cart.CartItem;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.persistence.dao.*;
import cart.persistence.entity.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cart.persistence.repository.Mapper.memberEntityMapper;
import static cart.persistence.repository.Mapper.orderHistoryEntityMapper;

@Component
public class PayRepository {

    private final MemberDao memberDao;
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;
    private final OrderHistoryDao orderHistoryDao;
    private final OrderProductDao orderProductDao;

    public PayRepository(final MemberDao memberDao, final ProductDao productDao, final CartItemDao cartItemDao, final OrderHistoryDao orderHistoryDao, final OrderProductDao orderProductDao) {
        this.memberDao = memberDao;
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
        this.orderHistoryDao = orderHistoryDao;
        this.orderProductDao = orderProductDao;
    }

    public List<CartItem> getCartItemsByIds(final List<Long> ids) {
        final List<CartItemEntity> cartItemEntities = cartItemDao.findByIds(ids);
        final Map<Long, MemberEntity> memberEntityMap = findMemberEntityMapByCartItemEntities(cartItemEntities);
        final List<ProductEntity> productEntities = getProductEntitiesFromCartItemEntities(cartItemEntities);
        return combineToCartItems(cartItemEntities, memberEntityMap, productEntities);
    }

    private Map<Long, MemberEntity> findMemberEntityMapByCartItemEntities(final List<CartItemEntity> cartItemEntities) {
        final List<Long> memberIds = cartItemEntities.stream()
                .map(CartItemEntity::getMemberId)
                .collect(Collectors.toList());
        final List<MemberEntity> memberEntities = memberDao.getMembersByIds(memberIds);
        return memberEntities.stream()
                .collect(Collectors.toMap(MemberEntity::getId, Function.identity()));
    }

    private List<CartItem> combineToCartItems(final List<CartItemEntity> cartItemEntities,
                                              final Map<Long, MemberEntity> memberEntityMap,
                                              final List<ProductEntity> productEntities) {
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
        return productDao.findByIds(productIds);
    }

    public Long createOrder(final Order order) {
        final Member member = order.getMember();
        final OrderHistoryEntity orderHistoryEntity = orderHistoryEntityMapper(order);
        final Long orderId = orderHistoryDao.createOrder(orderHistoryEntity);
        member.savePoint(order.getSavedPoint());
        final List<OrderProductEntity> orderProductEntities = order.getOrderProducts().getProducts().stream()
                .map(orderProduct -> Mapper.orderProductEntityMapper(orderProduct, orderId))
                .collect(Collectors.toList());
        orderProductDao.createProducts(orderProductEntities);
        updatePoint(member);
        return orderId;
    }

    private void updatePoint(final Member member) {
        final MemberEntity memberEntity = memberEntityMapper(member);
        memberDao.updatePoint(memberEntity);
    }

    public void deleteCartItemsByIds(final List<Long> cartIds) {
        cartItemDao.deleteAllById(cartIds);
    }
}
