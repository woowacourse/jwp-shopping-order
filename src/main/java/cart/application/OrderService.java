package cart.application;

import java.sql.Timestamp;
import java.util.List;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderService {

    private final PointRepository pointRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    public OrderService(final PointRepository pointRepository, final CartItemRepository cartItemRepository, final OrderRepository orderRepository) {
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
        final Order order = orderRepository.createOrder(member, new Order(cartItems, orderPoint, createdAt));
        return OrderResponse.of(order);
    }
}
