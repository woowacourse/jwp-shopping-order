package cart.repository;

import cart.dao.OrderDao;
import cart.dao.ProductOrderDao;
import cart.domain.Order;
import cart.domain.Product;
import cart.domain.Products;
import cart.entity.OrderEntity;
import cart.entity.ProductOrderEntity;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcOrderRepository implements OrderRepository {

    private final OrderDao orderDao;
    private final ProductOrderDao productOrderDao;

    public JdbcOrderRepository(final OrderDao orderDao, final ProductOrderDao productOrderDao) {
        this.orderDao = orderDao;
        this.productOrderDao = productOrderDao;
    }

    @Override
    public Order create(final Order order, final Long memberId) {
        final Long orderId = orderDao.create(OrderEntity.of(order, memberId), memberId);
        final Products products = order.getProducts();
        for (final Product product : products.getValue()) {
            productOrderDao.create(ProductOrderEntity.of(product.getId(), orderId));
        }
        return new Order(orderId, order.getProducts(), order.getCoupon(), order.getDeliveryAmount(),
                order.getAddress());
    }
}
