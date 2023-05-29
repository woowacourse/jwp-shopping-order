package cart.repository;

import cart.dao.OrderDao;
import cart.dao.OrderProductDao;
import cart.dao.ProductDao;
import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import cart.domain.member.MemberPoint;
import cart.domain.orderproduct.Order;
import cart.domain.orderproduct.OrderProduct;
import cart.domain.product.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final ProductDao productDao;
    private final OrderProductDao orderProductDao;

    public OrderRepository(final OrderDao orderDao, final ProductDao productDao, final OrderProductDao orderProductDao) {
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.orderProductDao = orderProductDao;
    }

    public Long save(final CartItems cartItems, final Member member, final MemberPoint usedPoint) {
        final Long orderId = orderDao.save(new Order(member, usedPoint));
        final Order findOrder = orderDao.findById(orderId);
        final List<Product> products = productDao.getProductsByIds(cartItems.getProductIds());
        final List<OrderProduct> orderProducts = cartItems.toOrderProducts(findOrder, products);
        orderProductDao.save(orderProducts);

        return orderId;
    }

    public Order findOrderById(final Long orderId) {
        return orderDao.findById(orderId);
    }

    public OrderProduct findOrderProductByOrderId(final Long orderProductId) {

//        return orderProductDao.findByOrderProductId(orderProductId);
        return null;
    }

    public List<OrderProduct> findAllOrderProductsByMemberId(final Long memberId) {

        return orderProductDao.findByMemberId(memberId);
    }

    public List<OrderProduct> findAllOrderProductsByOrderId(final Long orderId) {

        return orderProductDao.findByOrderId(orderId);
    }
}
