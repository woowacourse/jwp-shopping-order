package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.PaymentDao;
import cart.dao.ProductDao;
import cart.domain.CartItems;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.OrderItems;
import cart.domain.Payment;
import cart.domain.Price;
import cart.domain.Product;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import cart.entity.PaymentEntity;
import cart.entity.ProductEntity;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderDao orderDao;
    private final PaymentDao paymentDao;
    private final OrderItemDao orderItemDao;
    private final CartItemDao cartItemDao;
    private final ProductDao productDao;

    public OrderRepositoryImpl(final OrderDao orderDao, final PaymentDao paymentDao, final OrderItemDao orderItemDao,
                               final CartItemDao cartItemDao, final ProductDao productDao) {
        this.orderDao = orderDao;
        this.paymentDao = paymentDao;
        this.orderItemDao = orderItemDao;
        this.cartItemDao = cartItemDao;
        this.productDao = productDao;
    }

    public Long createOrder(final Order order, final CartItems cartItems) {
        Long orderId = orderDao.save(order);
        paymentDao.save(order.getPayment(), orderId, order.getMember().getId());
        orderItemDao.saveAll(order.getOrderItems(), orderId);
        cartItemDao.deleteAll(cartItems);
        return orderId;
    }

    public CartItems findCartItemsByMemberId(Long memberId) {
        return cartItemDao.findByMemberId(memberId);
    }

    public Order findOrder(Long orderId, Member member) {
        OrderEntity orderEntity = orderDao.findById(orderId);
        validateCorrectMemberById(orderEntity.getMemberId(), member.getId());
        return new Order(orderEntity.getId(),
                orderEntity.getCreatedAt(),
                createPayment(orderId),
                createOrderItems(orderId),
                member);
    }

    private Payment createPayment(final Long orderId) {
        PaymentEntity paymentEntity = paymentDao.findByOrderId(orderId);
        return new Payment(paymentEntity.getId(),
                new Price(paymentEntity.getOriginalPrice()),
                new Price(paymentEntity.getDiscountPrice()),
                new Price(paymentEntity.getFinalPrice()),
                paymentEntity.getCreatedAt());
    }

    private OrderItems createOrderItems(final Long orderId) {
        List<OrderItemEntity> orderItemEntities = orderItemDao.findAllByOrderId(orderId);
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemEntity orderItemEntity : orderItemEntities) {
            Product product = createProduct(orderItemEntity.getProductId());
            OrderItem orderItem = new OrderItem(orderItemEntity.getId(), product, orderItemEntity.getQuantity());
            orderItems.add(orderItem);
        }
        return new OrderItems(orderItems);
    }

    private Product createProduct(final Long productId) {
        ProductEntity productEntity = productDao.getProductById(productId);
        return new Product(productEntity.getId(),
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getImageUrl());
    }

    private void validateCorrectMemberById(Long memberId, Long otherId) {
        if (!memberId.equals(otherId)) {
            throw new IllegalArgumentException("주문한 사용자의 정보가 잘못되었습니다.");
        }
    }
}
