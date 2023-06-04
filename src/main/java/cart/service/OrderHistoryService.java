package cart.service;

import cart.domain.Member;
import cart.entity.OrderCartItemEntity;
import cart.entity.OrderEntity;
import cart.dto.order.OrderDetailHistoryResponse;
import cart.dto.order.OrderHistoryResponse;
import cart.dto.order.OrderItemHistoryResponse;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderHistoryService {
    private final OrderRepository orderRepository;

    public OrderHistoryService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<OrderHistoryResponse> readOrderHistory(Member member) {
        List<OrderHistoryResponse> orderHistoryResponses = new ArrayList<>();
        List<OrderEntity> orderEntities = orderRepository.findAllOrder(member);
        for (OrderEntity orderEntity : orderEntities) {
            List<OrderItemHistoryResponse> orderItemHistories = orderRepository.findOrderItemsByOrderId(orderEntity.getId()).stream()
                    .map(OrderItemHistoryResponse::from)
                    .collect(Collectors.toList());
            orderHistoryResponses.add(
                    new OrderHistoryResponse(orderEntity.getId(), orderEntity.getTime().toString(), orderItemHistories));
        }
        return orderHistoryResponses;
    }

    public OrderDetailHistoryResponse readDetailHistory(Member member, Long orderId) {
        OrderEntity orderEntity = orderRepository.findAllOrder(member).stream()
                .filter(s -> s.getId().equals(orderId))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 회원에게 해당 주문내역이 존재하지 않습니다."));

        List<OrderCartItemEntity> cartItemEntities = orderRepository.findOrderItemsByOrderId(orderId);
        return new OrderDetailHistoryResponse(
                orderEntity.getId(),
                orderEntity.getTime().toString(),
                cartItemEntities.stream().map(OrderItemHistoryResponse::from).collect(Collectors.toList()),
                orderEntity.getDeliveryPrice(),
                orderEntity.getDiscountFromTotal()
        );
    }


}
