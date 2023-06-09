package cart.application;

import cart.domain.*;
import cart.domain.pointRewardPolicy.PointRewardPolicy;
import cart.domain.repository.CartItemRepository;
import cart.domain.repository.OrderRepository;
import cart.dto.*;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final PointRewardPolicy pointRewardPolicy;
    private final CartItemRepository cartItemRepository;

    public OrderService(final OrderRepository orderRepository, final PointRewardPolicy pointRewardPolicy, final CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.pointRewardPolicy = pointRewardPolicy;
        this.cartItemRepository = cartItemRepository;
    }

    public Long addOrder(final Member member, final OrderRequest orderRequest) {
        final List<CartItem> cartItems = orderRequest.getOrderItems().stream()
                .map(orderItem -> cartItemRepository.findCartItemById(orderItem.getCartItemId()))
                .collect(Collectors.toList());
        final List<OrderDetail> orderDetails = cartItems.stream()
                .map(cartItem -> new OrderDetail(cartItem.getProduct(),(long)cartItem.getQuantity()))
                .collect(Collectors.toList());

        final Order order = new Order(orderDetails, new Payment(BigDecimal.valueOf(orderRequest.getPayment().getFinalPayment())), new Point(BigDecimal.valueOf(orderRequest.getPayment().getPoint())), member);
        //포인트 차감 및 적립
        final Point originalPoint = member.getPoint();
        final Point usedPoint = order.getPoint();
        final Point addedPoint = new Point(pointRewardPolicy.calculate(order.getPayment()));
        final Point newPoint = originalPoint.subtractPoint(usedPoint).addPoint(addedPoint);
        orderRepository.updateMemberPoint(member, newPoint);
        //주문 저장
        final Long orderId = orderRepository.saveOrder(order);
        //장바구니 물품 삭제
        for (CartItem cartItem : cartItems) {
            cartItemRepository.deleteCartItemById(cartItem.getId());
        }
        return orderId;
    }

    public OrderResponses showOrders(final Member member, final Integer page, final Integer size) {
        List<Order> orders = orderRepository.findOrdersByMemberId(member)
                .stream()
                .sorted(Comparator.comparing(Order::getOrderId).reversed())
                .collect(Collectors.toList());
        final List<OrderResponse> orderResponses = orders.stream()
                .map(order -> getOrderResponse(order)).collect(Collectors.toList());
        final List<List<OrderResponse>> partitions = Lists.partition(orderResponses, size);
        final List<OrderResponse> target = partitions.get(page - 1);
        final PageInfo pageInfo = new PageInfo(page, size, orders.size(), partitions.size());
        return new OrderResponses(pageInfo,target);

    }

    public OrderResponse showOrder(final Member member, final Long orderId) {
        Order order = orderRepository.findOrderByOrderId(orderId, member);
        return getOrderResponse(order);
    }

    private OrderResponse getOrderResponse(final Order order) {
        return new OrderResponse(
                order.getOrderId(),
                order.getOrderDetails().stream()
                        .map(orderDetail -> new OrderedProduct(
                                orderDetail.getProduct().getName(),
                                (long) orderDetail.getProduct().getPrice(),
                                orderDetail.getQuantity(),
                                orderDetail.getProduct().getImageUrl()
                        )).collect(Collectors.toList()),
                new PaymentDto(
                        order.getPayment().getPayment().add(order.getPoint().getPoint()).longValue(),
                        order.getPayment().getPayment().longValue(),
                        order.getPoint().getPoint().longValue())
        );
    }
}
