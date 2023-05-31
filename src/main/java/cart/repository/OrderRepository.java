package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.dao.OrderProductDao;
import cart.dao.ProductDao;
import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import cart.domain.member.MemberPoint;
import cart.domain.orderproduct.DeliveryFee;
import cart.domain.orderproduct.Order;
import cart.domain.orderproduct.OrderProduct;
import cart.domain.product.Product;
import cart.exception.notfound.OrderNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final ProductDao productDao;
    private final OrderProductDao orderProductDao;
    private final CartItemDao cartItemDao;

    public OrderRepository(final OrderDao orderDao, final ProductDao productDao,
                           final OrderProductDao orderProductDao, final CartItemDao cartItemDao) {
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.orderProductDao = orderProductDao;
        this.cartItemDao = cartItemDao;
    }

    public Long save(final CartItems cartItems, final Member member, final MemberPoint usedPoint) {
        final Long orderId = orderDao.insert(new Order(member, usedPoint, new DeliveryFee(cartItems.getDeliveryFee())));
        final Order findOrder = orderDao.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        final List<Product> products = productDao.findAllByIds(cartItems.getProductIds());
        final List<OrderProduct> orderProducts = cartItems.toOrderProducts(findOrder, products);
        orderProductDao.insertAll(orderProducts);
        cartItemDao.deleteByIds(cartItems.getCartItemIds());

        return orderId;
    }

    public Order findById(final Long orderId) {
        return orderDao.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    public List<OrderProduct> findAllByOrderId(final Long orderId) {
        return orderProductDao.findAllByOrderId(orderId);
    }

    public List<Order> findAllByMemberId(final Long memberId) {
        return orderDao.findAllByMemberId(memberId);
    }
}
