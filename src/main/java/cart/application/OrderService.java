package cart.application;

import cart.dao.CartItemDao;
import cart.domain.Member;
import cart.domain.point.Point;
import cart.domain.ShippingDiscountPolicy;
import cart.domain.ShippingFee;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.point.PointPolicyStrategy;
import cart.dto.order.OrderCreateResponse;
import cart.dto.order.OrderRequest;
import cart.dto.order.OrderResponse;
import cart.dto.order.OrdersResponse;
import cart.dto.orderpolicy.OrderPolicyResponse;
import cart.repository.OrderRepository;
import cart.repository.PointRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service

public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemDao cartItemDao;
    private final PointRepository pointRepository;
    private final PointPolicyStrategy pointPolicyStrategy;

    public OrderService(OrderRepository orderRepository, CartItemDao cartItemDao, PointRepository pointRepository, PointPolicyStrategy pointPolicyStrategy) {
        this.orderRepository = orderRepository;
        this.cartItemDao = cartItemDao;
        this.pointRepository = pointRepository;
        this.pointPolicyStrategy = pointPolicyStrategy;
    }

    public OrderCreateResponse createOrder(final Member member, final OrderRequest orderRequest) {
        final List<OrderItem> orderItemList = orderRequest.getOrder().stream()
                .map(orderItemDto -> new OrderItem(cartItemDao.findById(orderItemDto.getCartItemId()).getProduct(), orderItemDto.getQuantity()))
                .collect(toList());

        // TODO: optional 예외처리 구현하기
        final ShippingFee shippingFee = orderRepository.findShippingFee();
        final ShippingDiscountPolicy shippingDiscountPolicy = orderRepository.findShippingDiscountPolicy();

        final Order newOrder = Order.of(member,
                shippingFee.getFee(),
                orderItemList,
                shippingDiscountPolicy.getThreshold(),
                new Point(orderRequest.getUsedPoint()));
        final Long orderId = orderRepository.saveOrder(member, newOrder);
        final Point memberPoint = pointRepository.findPointByMemberId(member.getId());

        pointRepository.updatePoint(member.getId(), memberPoint.minus(orderRequest.getUsedPoint()));
        final Long earnedPoint = pointPolicyStrategy.caclulatePointWithPolicy(newOrder.getTotalPrice() - orderRequest.getUsedPoint());
        pointRepository.updatePoint(member.getId(), earnedPoint);

        return new OrderCreateResponse(orderId, earnedPoint);
    }

    public List<OrdersResponse> findAllOrdersByMember(final Member member) {
        final List<Order> orders = orderRepository.findAllOrdersByMember(member);
        return orders.stream()
                .map(OrdersResponse::from)
                .collect(toList());
    }

    public OrderResponse findOrderDetailsByOrderId(final Member member, final Long orderId) {
        final Order order = orderRepository.findOrderById(member, orderId);
        return OrderResponse.from(order);
    }

}
