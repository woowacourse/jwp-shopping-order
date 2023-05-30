package cart.repository.mapper;

import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderProductEntity;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static Order toDomain(OrderEntity orderEntity, List<OrderProductEntity> orderProductEntities) {
        List<OrderProduct> orderProducts = orderProductEntities.stream()
                .map(OrderProductMapper::toDomain)
                .collect(Collectors.toList());
        return new Order(
                orderEntity.getId(),
                MemberMapper.toDomain(orderEntity.getMemberEntity()),
                orderProducts,
                orderEntity.getUsedPoint(),
                orderEntity.getCreatedAt()
        );
    }

    public static OrderEntity toEntity(Order order) {
        return new OrderEntity(
                order.getId(),
                MemberMapper.toEntity(order.getMember()),
                order.getUsedPoint()
        );
    }
}
