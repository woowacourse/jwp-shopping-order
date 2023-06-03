package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.PaymentDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.CartItems;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.OrderItems;
import cart.domain.Payment;
import cart.domain.Price;
import cart.domain.Product;
import cart.entity.CartItemEntity;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import cart.entity.PaymentEntity;
import cart.entity.ProductEntity;
import java.util.List;
import java.util.stream.Collectors;
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

    public CartItems findCartItemsByMemberId(Member member) {
        List<CartItemEntity> cartItemEntity = cartItemDao.findByMemberId(member.getId());
        return createCartItems(member, cartItemEntity);
    }

    private CartItems createCartItems(final Member member, final List<CartItemEntity> cartItemEntities) {
        return new CartItems(cartItemEntities.stream()
                .map(cartItemEntity -> createCartItem(member, cartItemEntity))
                .collect(Collectors.toList()));
    }

    private CartItem createCartItem(final Member member, final CartItemEntity cartItemEntity) {
        return new CartItem(
                cartItemEntity.getId(),
                cartItemEntity.getQuantity(),
                convertProductEntityToProduct(cartItemEntity.getProductId()),
                member);
    }

    private Product convertProductEntityToProduct(final Long productId) {
        ProductEntity productEntity = productDao.getProductById(productId);
        return new Product(productEntity.getId(),
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getImageUrl());
    }

    public Order findOrder(Long orderId, Member member) {
        OrderEntity orderEntity = orderDao.findById(orderId);
        validateCorrectMemberById(orderEntity.getMemberId(), member.getId());

        return buildOrder(member, orderEntity);
    }

    private Order buildOrder(final Member member, final OrderEntity orderEntity) {
        return new Order(orderEntity.getId(),
                orderEntity.getCreatedAt(),
                createPayment(orderEntity.getId()),
                createOrderItems(orderEntity.getId()),
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
        List<OrderItem> orderItems = orderItemEntities.stream()
                .map(orderItemEntity -> new OrderItem(orderItemEntity.getId(),
                        createProduct(orderItemEntity.getProductId()),
                        orderItemEntity.getQuantity()))
                .collect(Collectors.toList());
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

    public List<Order> findAllByMember(Member member) {
        List<OrderEntity> orderEntities = orderDao.findAllByMemberId(member.getId());
        return orderEntities.stream()
                .map(orderEntity -> buildOrder(member, orderEntity))
                .collect(Collectors.toList());
    }
}
