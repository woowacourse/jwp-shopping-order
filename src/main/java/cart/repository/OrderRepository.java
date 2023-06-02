package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.PaymentDao;
import cart.dao.ProductDao;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Payment;
import cart.domain.Product;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;
    private final MemberDao memberDao;
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final PaymentDao paymentDao;

    public OrderRepository(ProductDao productDao, CartItemDao cartItemDao, MemberDao memberDao, OrderDao orderDao,
                           OrderItemDao orderItemDao, PaymentDao paymentDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
        this.memberDao = memberDao;
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.paymentDao = paymentDao;
    }

    public Long order(List<Long> cartItemIds, Member member, Order order) {
        cartItemDao.deleteByIds(cartItemIds);
        List<Product> products = order.getOrderItems()
                .stream()
                .map(it -> it.getOriginalProduct())
                .collect(Collectors.toList());
        productDao.updateStocks(products);
        memberDao.updatePoint(member);
        Long orderId = orderDao.save(member.getId());
        orderItemDao.saveAll(orderId, order.getOrderItems());
        return orderId;
    }

    public Order findById(Long orderId) {
        Order order = orderDao.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));
        return assemble(order);
    }

    public List<Order> findByMemberId(Long memberId) {
        List<Order> orders = orderDao.findByMemberId(memberId);
        return orders.stream()
                .map(this::assemble)
                .collect(Collectors.toList());
    }

    public Order assemble(Order order) {
        List<OrderItem> orderItems = orderItemDao.findByOrderId(order.getId());
        return new Order(order.getId(), order.getMember(), orderItems, order.getOrderDateTime());
    }
}
