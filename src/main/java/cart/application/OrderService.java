package cart.application;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import cart.domain.CartItems;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderPoint;
import cart.domain.Point;
import cart.domain.Price;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.repository.CartItemRepository;
import cart.repository.OrderRepository;
import cart.repository.PointRepository;
import cart.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final PointRepository pointRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    public OrderService(
            final ProductRepository productRepository,
            final PointRepository pointRepository,
            final CartItemRepository cartItemRepository,
            final OrderRepository orderRepository
    ) {
        this.productRepository = productRepository;
        this.pointRepository = pointRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
    }

    public OrderResponse createOrder(final Member member, final OrderRequest orderRequest) {
        final List<Long> cartItemIds = orderRequest.getCartIds();
        final Point usedPoint = Point.valueOf(orderRequest.getPoint());
        final Price totalPrice = Price.valueOf(orderRequest.getTotalPrice());
        return createOrder(member, cartItemIds, usedPoint, totalPrice);
    }

    private OrderResponse createOrder(final Member member, final List<Long> cartItemIds, final Point usedPoint, final Price totalPrice) {
        final CartItems cartItems = cartItemRepository.findByIds(member, cartItemIds);
        cartItems.validateTotalPrice(totalPrice);
        final Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        final OrderPoint orderPoint = pointRepository.updatePoint(member, usedPoint, cartItems.getTotalPrice(), createdAt);
        productRepository.updateStock(cartItems);
        final Order order = orderRepository.createOrder(member, Order.of(cartItems, orderPoint, createdAt));
        return OrderResponse.of(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrders(final Member member) {
        final List<Order> orders = orderRepository.findByMember(member);
        return orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(final Member member, final Long id) {
        final Order order = orderRepository.findById(member, id);
        return OrderResponse.of(order);
    }
}
