package cart.repository;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.OrderItems;
import cart.domain.Price;
import cart.domain.Product;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import cart.entity.OrderItemWithProductEntity;
import cart.exception.OrderItemNoContentException;
import cart.repository.dto.OrderAndMainProductDto;
import cart.repository.dto.OrderInfoDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderJdbcRepository implements OrderRepository {

    private static final int SIZE_OF_NO_CONTENT = 0;
    private static final int MAIN_ENTITY_INDEX = 0;

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public OrderJdbcRepository(final OrderDao orderDao, final OrderItemDao orderItemDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
    }

    @Override
    public long save(final Order order) {
        final long orderId = orderDao.insert(toOrderEntity(order));
        orderItemDao.insertAll(mapToOrderItemEntities(orderId, order.getOrderItems()));
        return orderId;
    }

    @Override
    public List<OrderAndMainProductDto> findByMember(final Member member) {
        final List<OrderEntity> orderEntities = orderDao.findByMemberId(member.getId());
        return orderEntities.stream()
                .map(this::makeOrderAndMainProductDto)
                .collect(Collectors.toUnmodifiableList());
    }

    private OrderAndMainProductDto makeOrderAndMainProductDto(final OrderEntity orderEntity) {
        final List<OrderItemWithProductEntity> orderItemWithProductEntities = orderItemDao.findProductDetailByOrderId(orderEntity.getId());
        validateOrderItemsSize(orderItemWithProductEntities);
        final OrderItemWithProductEntity mainOrderItemEntity = orderItemWithProductEntities.remove(MAIN_ENTITY_INDEX);
        final Product mainProduct = new Product(mainOrderItemEntity.getProductId(), mainOrderItemEntity.getProductName(),
                new Price(mainOrderItemEntity.getProductPrice()), mainOrderItemEntity.getProductImageUrl());
        return new OrderAndMainProductDto(
                OrderInfoDto.from(orderEntity),
                mainProduct,
                orderItemWithProductEntities.size()
        );
    }

    private void validateOrderItemsSize(final List<OrderItemWithProductEntity> entities) {
        if (entities.size() == SIZE_OF_NO_CONTENT) {
            throw new OrderItemNoContentException();
        }
    }

    private OrderEntity toOrderEntity(final Order order) {
        return new OrderEntity(order.getMember().getId(), order.getOriginalPrice(), order.getDiscountPrice());
    }

    private List<OrderItemEntity> mapToOrderItemEntities(final long orderId, final OrderItems orderItems) {
        return orderItems.getOrderItems().stream()
                .map(orderItem -> toOrderItemEntity(orderId, orderItem))
                .collect(Collectors.toUnmodifiableList());
    }

    private OrderItemEntity toOrderItemEntity(final long orderId, final OrderItem orderItem) {
        return new OrderItemEntity(orderId, orderItem.getProduct().getId(), orderItem.getQuantity().getValue());
    }
}
