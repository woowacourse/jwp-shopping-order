package cart.repository;

import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.domain.coupon.Coupon;
import cart.domain.repository.OrderRepository;
import cart.entity.OrderEntity;
import cart.exception.OrderException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderDao orderDao;
    private final MemberDao memberDao;
    private final OrderProductDao orderProductDao;

    public OrderRepositoryImpl(OrderDao orderDao, MemberDao memberDao, OrderProductDao orderProductDao) {
        this.orderDao = orderDao;
        this.memberDao = memberDao;
        this.orderProductDao = orderProductDao;
    }

    @Override
    public Long saveOrder(Order order) {
        return orderDao.saveOrder(toEntity(order));
    }

    @Override
    public List<Order> findAllByMemberId(Member member) {
        return orderDao.findAllByMemberId(member.getId()).stream()
                .map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public Order findByOrderId(Member member, Long orderId) {
        return toDomain(orderDao.findByOrderId(member.getId(),orderId).orElseThrow(() -> new OrderException("주문 상세정보를 찾을 수 없습니다.")));
    }

    @Override
    public void deleteOrder(Long orderId) {
        orderDao.deleteOrderById(orderId);
    }

    private OrderEntity toEntity(Order order) {
        return new OrderEntity(
                order.getMember().getId(),
                order.calculatePrice(),
                order.calculateDiscountPrice());
    }

    private Order toDomain(OrderEntity orderEntity) {
        Member member = memberDao.getMemberById(orderEntity.getMemberId());
        List<CartItem> products = orderProductDao.findAllByOrderId(orderEntity.getId()).stream()
                .map(it -> new CartItem(it.getId(), it.getQuantity(),
                        new Product(it.getId(), it.getName(), it.getPrice(), it.getImage_url()), member))
                .collect(Collectors.toList());

        return new Order(orderEntity.getId(), member, products, orderEntity.getConfirmState(), Coupon.empty());
    }
}
