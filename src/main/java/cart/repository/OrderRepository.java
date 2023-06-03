package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.PaymentDao;
import cart.dao.ProductDao;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Product;
import cart.exception.notFound.OrderNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public OrderRepository(ProductDao productDao, CartItemDao cartItemDao, OrderDao orderDao,
                           OrderItemDao orderItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
    }

    public Long order(List<Long> cartItemIds, Long memberId, Order order) {
        cartItemDao.deleteByIds(cartItemIds);
        List<Product> products = order.getOrderItems()
                .stream()
                .map(OrderItem::getOriginalProduct)
                .collect(Collectors.toList());
        productDao.updateStocks(products);
        Long orderId = orderDao.save(memberId);
        orderItemDao.saveAll(orderId, order.getOrderItems());
        return orderId;
    }

    public Order findById(Long orderId) {
        Order order = orderDao.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
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
