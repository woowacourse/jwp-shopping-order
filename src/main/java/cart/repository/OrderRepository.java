package cart.repository;

import static cart.exception.ErrorMessage.NOT_FOUND_ORDER;

import cart.dao.OrderDao;
import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderWithOrderProductEntities;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.exception.OrderException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;

    public OrderRepository(final OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public Long save(Order order) {
        OrderEntity orderEntity = toEntity(order);

        return orderDao.save(orderEntity);
    }

    private OrderEntity toEntity(Order order) {
        return new OrderEntity(
                order.getId(), order.getMember().getId(),
                order.getUsedPoint(), null, null
        );
    }

    public Order findById(Long orderId, Member member) {
        OrderWithOrderProductEntities orderEntity = orderDao.findById(orderId)
                .orElseThrow(() -> new OrderException(NOT_FOUND_ORDER));

        return toDomain(orderEntity, member);
    }

    public List<Order> findOrdersByMemberId(Member member) {
        List<OrderWithOrderProductEntities> orderEntities = orderDao.findOrdersByMemberId(member.getId());

        return orderEntities.stream()
                .map(orderEntity -> toDomain(orderEntity, member))
                .collect(Collectors.toList());
    }

    private Order toDomain(OrderWithOrderProductEntities orderWithOrderProductEntities, Member member) {
        OrderEntity orderEntity = orderWithOrderProductEntities.getOrderEntity();
        List<Product> products = orderWithOrderProductEntities.getOrderProductEntities().stream()
                .map(productEntity -> new Product(
                        productEntity.getId(),
                        productEntity.getProductName(),
                        productEntity.getProductPrice(),
                        productEntity.getProductImageUrl())
                ).collect(Collectors.toList());

        return new Order(orderEntity.getId(), products, member, orderEntity.getUsedPoint());
    }
}
