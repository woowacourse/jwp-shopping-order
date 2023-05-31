package cart.repository.mapper;

import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderProductEntity;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static Order toDomain(OrderEntity orderEntity, List<OrderProductEntity> orderProductEntities) {
        return new Order(
                orderEntity.getId(),
                MemberMapper.toDomain(orderEntity.getMemberEntity()),
                generateOrderProducts(orderProductEntities),
                orderEntity.getUsedPoint(),
                orderEntity.getDeliveryFee(),
                orderEntity.getCreatedAt()
        );
    }

    private static List<OrderProduct> generateOrderProducts(List<OrderProductEntity> orderProductEntities) {
        return orderProductEntities.stream()
                .map(OrderProductMapper::toDomain)
                .collect(Collectors.toList());
    }

    public static OrderEntity toEntity(Order order) {
        return new OrderEntity(
                order.getId(),
                MemberMapper.toEntity(order.getMember()),
                order.getUsedPoint(),
                order.getDeliveryFee()
        );
    }
}
