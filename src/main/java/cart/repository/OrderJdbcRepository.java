package cart.repository;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.dto.OrderProductDto;
import cart.domain.discount.strategy.DiscountPriceCalculator;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.OrderItems;
import cart.domain.Price;
import cart.domain.Product;
import cart.domain.Quantity;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import cart.dao.dto.OrderItemWithProductDto;
import cart.exception.OrderItemNoContentException;
import cart.domain.OrderInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class OrderJdbcRepository implements OrderRepository {

    private static final int SIZE_OF_NO_CONTENT = 0;

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
    public List<OrderInfo> findByMember(final Member member) {
        final List<OrderEntity> orderEntities = orderDao.findByMemberId(member.getId());
        return orderEntities.stream()
                .map(orderEntity -> new OrderInfo(
                        orderEntity.getId(), orderEntity.getOriginalPrice(),
                        orderEntity.getDiscountPrice(), orderEntity.getCreatedAt()))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Map<Long, List<Product>> findProductsByIds(final List<Long> ids) {
        final List<OrderProductDto> orderProducts = orderItemDao.findProductByOrderIds(ids);
        return orderProducts.stream()
                .collect(Collectors.groupingBy(OrderProductDto::getOrderId))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> toProducts(entry.getValue())
                ));
    }

    private List<Product> toProducts(final List<OrderProductDto> orderProductDtos) {
        validateOrderProductsSize(orderProductDtos);
        return orderProductDtos.stream()
                .map(orderProductDto -> new Product(
                        orderProductDto.getProductId(), orderProductDto.getProductName(),
                        new Price(orderProductDto.getPrice()), orderProductDto.getImageUrl()))
                .collect(Collectors.toUnmodifiableList());
    }

    private void validateOrderProductsSize(final List<OrderProductDto> orderProducts) {
        if (orderProducts.size() == SIZE_OF_NO_CONTENT) {
            throw new OrderItemNoContentException();
        }
    }

    @Override
    public Order findById(final long id) {
        final OrderEntity orderEntity = orderDao.findById(id);
        final List<OrderItemWithProductDto> orderItemWithProductEntities = orderItemDao.findProductDetailByOrderId(id);
        return toOrderDomain(orderEntity, orderItemWithProductEntities);
    }

    private OrderEntity toOrderEntity(final Order order) {
        return new OrderEntity(order.getMemberId(), order.getOriginalPrice(), order.getDiscountPrice());
    }

    private List<OrderItemEntity> mapToOrderItemEntities(final long orderId, final OrderItems orderItems) {
        return orderItems.getOrderItems().stream()
                .map(orderItem -> toOrderItemEntity(orderId, orderItem))
                .collect(Collectors.toUnmodifiableList());
    }

    private OrderItemEntity toOrderItemEntity(final long orderId, final OrderItem orderItem) {
        return new OrderItemEntity(orderId, orderItem.getProduct().getId(), orderItem.getQuantity().getValue());
    }

    private Order toOrderDomain(final OrderEntity orderEntity, final List<OrderItemWithProductDto> orderItemWithProductEntities) {
        return new Order(
                orderEntity.getId(), orderEntity.getMemberId(),
                new OrderItems(mapToOrderItems(orderItemWithProductEntities), new DiscountPriceCalculator()),
                orderEntity.getCreatedAt()
        );
    }

    private List<OrderItem> mapToOrderItems(final List<OrderItemWithProductDto> orderItemWithProductEntities) {
        return orderItemWithProductEntities.stream()
                .map(entity -> new OrderItem(
                        entity.getId(),
                        new Product(entity.getProductId(), entity.getProductName(),
                                new Price(entity.getProductPrice()), entity.getProductImageUrl()),
                        new Quantity(entity.getQuantity())
                ))
                .collect(Collectors.toUnmodifiableList());
    }
}
