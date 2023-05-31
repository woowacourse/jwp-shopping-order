package cart.repository;

import cart.dao.MemberCouponDao;
import cart.dao.MemberDao;
import cart.dao.OrdersDao;
import cart.dao.entity.OrdersEntity;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Orders;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrdersRepository {
    private final OrdersDao ordersDao;
    private final CartItemRepository cartItemRepository;
    private final RelativeRepository relativeRepository;
    private final MemberDao memberDao;

    public OrdersRepository(OrdersDao ordersDao, CartItemRepository cartItemRepository, RelativeRepository relativeRepository, MemberDao memberDao) {
        this.ordersDao = ordersDao;
        this.cartItemRepository = cartItemRepository;
        this.relativeRepository = relativeRepository;
        this.memberDao = memberDao;
    }

    public long takeOrders(
            final Long memberId,
            final List<Long> cartIds,
            final int originalPrice,
            final int discountPrice,
            final List<Long> couponIds) {
        final long orderId = ordersDao.createOrders(memberId, originalPrice, discountPrice);
        cartItemRepository.changeCartItemToOrdersItem(orderId, cartIds);
        relativeRepository.addOrdersCoupon(orderId, couponIds);
        return orderId;
    }

    public List<Orders> findAllOrdersByMember(Member member) {
        return ordersDao.findAllByMemberId(member.getId()).stream()
                .map(ordersEntity -> relativeRepository.makeOrders(ordersEntity, member))
                .collect(Collectors.toList());
    }

    public Orders findOrdersById(final Member member, final long id) {
        return relativeRepository.makeOrders(ordersDao.findById(id), member);
    }

    public Orders  confirmOrdersCreateCoupon(Member member,final long id) {
        ordersDao.updateConfirmById(id);
        return findOrdersById(member,id);
    }

    public void deleteOrders(final long id) {
        ordersDao.deleteById(id);
    }
}
