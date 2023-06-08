package cart.repository.mapper;

import cart.dao.dto.order.OrderItemProductDto;
import cart.dao.dto.order.OrderWithMemberDto;
import cart.dao.dto.order.OrderDto;
import cart.dao.dto.order.OrderItemDto;
import cart.domain.Member;
import cart.domain.Money;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.OrderProduct;
import cart.domain.Quantity;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderMapper {

    private OrderMapper() {
    }

    public static OrderItemDto toOrderItemDto(OrderItem orderItem, long orderId) {
        OrderProduct product = orderItem.getProduct();
        return new OrderItemDto(
            orderId,
            product.getProductId(),
            orderItem.getQuantityCount(),
            product.getPriceIntValue()
        );
    }

    public static OrderItem toOrderItem(OrderItemProductDto orderItemProductDto) {
        return new OrderItem(
            orderItemProductDto.getOrderItemId(),
            new OrderProduct(
                orderItemProductDto.getProductId(),
                orderItemProductDto.getProductName(),
                Money.from(orderItemProductDto.getPrice()),
                orderItemProductDto.getProductImageUrl()
            ),
            Quantity.from(orderItemProductDto.getQuantity())
        );
    }

    public static OrderDto toOrderDto(Order order) {
        return new OrderDto(
            order.getMemberId()
        );
    }

    public static Order toOrder(OrderWithMemberDto orderWithMemberDto,
        List<OrderItemProductDto> orderItemProducts) {
        List<OrderItem> orderItems = orderItemProducts.stream()
            .map(OrderMapper::toOrderItem)
            .collect(Collectors.toList());
        return new Order(
            orderWithMemberDto.getId(), orderItems, orderWithMemberDto.getCreatedAt(),
            new Member(
                orderWithMemberDto.getMemberId(),
                orderWithMemberDto.getEmail(),
                orderWithMemberDto.getPassword())
        );
    }

    public static List<Order> toOrdersSortedById(
        Map<OrderWithMemberDto, List<OrderItemProductDto>> orderProductsByOrder) {
        return orderProductsByOrder.entrySet()
            .stream()
            .map(orderWithItem -> OrderMapper.toOrder(orderWithItem.getKey(),
                orderWithItem.getValue()))
            .sorted(Comparator.comparing(Order::getId))
            .collect(Collectors.toList());
    }

}
