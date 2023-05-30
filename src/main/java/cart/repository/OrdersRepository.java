package cart.repository;

import cart.dao.OrdersDao;
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

    public OrdersRepository(OrdersDao ordersDao, CartItemRepository cartItemRepository, RelativeRepository relativeRepository) {
        this.ordersDao = ordersDao;
        this.cartItemRepository = cartItemRepository;
        this.relativeRepository = relativeRepository;
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

    public Coupon confirmOrdersCreateCoupon(final long id) {
        ordersDao.updateConfirmById(id);
        return null;
    }

    public void deleteOrders(final long id) {
        ordersDao.deleteById(id);
    }
}
