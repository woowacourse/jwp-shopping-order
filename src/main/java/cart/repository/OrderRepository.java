package cart.repository;

import cart.dao.OrderDao;
import cart.dao.OrderProductDao;
import cart.dao.ProductDao;
import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import cart.domain.member.MemberPoint;
import cart.domain.order.DeliveryFee;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.domain.product.Product;
import cart.exception.notfound.OrderNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final ProductDao productDao;
    private final OrderProductDao orderProductDao;

    public OrderRepository(final OrderDao orderDao, final ProductDao productDao,
                           final OrderProductDao orderProductDao) {
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.orderProductDao = orderProductDao;
    }

    public Long save(final CartItems cartItems, final Member member, final MemberPoint usedPoint) {
        final Order order = new Order(member, usedPoint, cartItems.getSavedPoint(), new DeliveryFee(cartItems.getDeliveryFee()));
        final Long orderId = orderDao.insert(order);
        final Order findOrder = orderDao.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        final List<Product> products = productDao.findAllByIds(cartItems.getProductIds());
        final List<OrderProduct> orderProducts = cartItems.toOrderProducts(findOrder, products);
        orderProductDao.insertAll(orderProducts);
        return orderId;
    }

    public Order findById(final Long orderId) {
        return orderDao.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    public List<OrderProduct> findAllByOrderId(final Long orderId) {
        return orderProductDao.findAllByOrderId(orderId);
    }

    public List<Order> findAllByOrderIds(final List<Long> ids) {
        return orderDao.findAllByIds(ids);
    }

    public List<Order> findAllByMemberId(final Long memberId) {
        return orderDao.findAllByMemberId(memberId);
    }

    public void deleteAll(final List<Order> orders) {
        orders.forEach(order -> orderProductDao.deleteAllByOrderId(order.getId()));
        final List<Long> orderIds = orders.stream()
                .map(Order::getId)
                .collect(Collectors.toList());
        orderDao.deleteByIds(orderIds);
    }
}
