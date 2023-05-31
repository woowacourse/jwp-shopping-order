package cart.application;

import cart.domain.*;
import cart.domain.pointRewardPolicy.PointRewardPolicy;
import cart.domain.repository.OrderRepository;
import cart.dto.EachOrderResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.dto.OrderedProduct;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final PointRewardPolicy pointRewardPolicy;

    public OrderService(final OrderRepository orderRepository, final PointRewardPolicy pointRewardPolicy) {
        this.orderRepository = orderRepository;
        this.pointRewardPolicy = pointRewardPolicy;
    }

    public Long addOrder(final Member member, final OrderRequest orderRequest) {
        final List<OrderDetail> orderDetails = orderRequest.getOrderInfos().stream()
                .map(orderInfo -> new OrderDetail(orderRepository.findProductById(orderInfo.getProductId()), orderInfo.getQuantity()))
                .collect(Collectors.toList());

        final Order order = new Order(orderDetails, new Payment(BigDecimal.valueOf(orderRequest.getPayment())), new Point(BigDecimal.valueOf(orderRequest.getPoint())), member);
        //포인트 차감 및 적립
        final Point originalPoint = member.getPoint();
        final Point usedPoint = order.getPoint();
        final Point addedPoint = new Point(pointRewardPolicy.calculate(order.getPayment()));
        final Point newPoint = originalPoint.subtractPoint(usedPoint).addPoint(addedPoint);
        orderRepository.updateMemberPoint(member, newPoint);
        //주문 저장
        final Long orderId = orderRepository.saveOrder(order);
        //장바구니 물품 삭제
        orderRepository.deleteCartItemByMember(member);
        return orderId;
    }

    public List<OrderResponse> showOrders(final Member member) {
        List<Order> orders = orderRepository.findOrdersByMemberId(member);
        return orders.stream()
                .map(order -> new OrderResponse(
                        order.getOrderId(),
                        order.getOrderDetails().stream()
                                .map(orderDetail -> new OrderedProduct(
                                        orderDetail.getProduct().getName(),
                                        Long.valueOf(orderDetail.getProduct().getPrice()),
                                        orderDetail.getQuantity(),
                                        orderDetail.getProduct().getImageUrl()
                                )).collect(Collectors.toList())
                )).collect(Collectors.toList());
    }

    public EachOrderResponse showOrder(final Member member, final Long orderId) {
        Order order = orderRepository.findOrderByOrderId(orderId, member);
        return new EachOrderResponse(
                order.getOrderDetails().stream()
                        .map(orderDetail -> new OrderedProduct(
                                orderDetail.getProduct().getName(),
                                Long.valueOf(orderDetail.getProduct().getPrice()),
                                orderDetail.getQuantity(),
                                orderDetail.getProduct().getImageUrl()
                                ))
                        .collect(Collectors.toList()),
                order.getPayment().getPayment().longValue(),
                order.getPoint().getPoint().longValue()
        );
    }
}
