package cart.repository;

import cart.dao.OrdersDao;
import cart.dao.entity.OrdersEntity;
import cart.domain.Member;
import cart.domain.Orders;
import cart.domain.Price;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrdersRepository {
    private final OrdersDao ordersDao;
    private final CartItemRepository cartItemRepository;

    public OrdersRepository(OrdersDao ordersDao, CartItemRepository cartItemRepository) {
        this.ordersDao = ordersDao;
        this.cartItemRepository = cartItemRepository;
    }

    // 분리 고민
    public long takeOrders(
            final Long memberId,
            final int discountPrice) {
        return ordersDao.createOrders(memberId, discountPrice);
    }

    public List<Orders> findAllOrdersByMember(final Member member) {
        return ordersDao.findAllByMemberId(member.getId()).stream()
                .map(this::rendering)
                .collect(Collectors.toList());
    }

    public Orders findOrdersById(final Member member, final long id) {
        OrdersEntity orders = ordersDao.findById(id);
        return rendering(orders);
    }

    public Orders  confirmOrdersCreateCoupon(Member member,final long id) {
        ordersDao.updateConfirmById(id);
        return findOrdersById(member,id);
    }

    public void deleteOrders(final long id) {
        ordersDao.deleteById(id);
    }

    private Orders rendering(OrdersEntity orders){
        return new Orders(
                orders.getId(),
                orders.getPrice(),
                orders.getConfirmState()
        );
    }
}
