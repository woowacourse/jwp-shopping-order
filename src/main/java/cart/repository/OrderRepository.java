package cart.repository;

import cart.dao.OrderHistoryDao;
import cart.dao.OrderProductDao;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.entity.OrderHistoryEntity;
import cart.entity.OrderProductEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {

    private final OrderHistoryDao orderHistoryDao;
    private final OrderProductDao orderProductDao;

    public OrderRepository(final OrderHistoryDao orderHistoryDao, final OrderProductDao orderProductDao) {
        this.orderHistoryDao = orderHistoryDao;
        this.orderProductDao = orderProductDao;
    }

    public List<OrderHistoryEntity> findAll(){
        return orderHistoryDao.findAllOrderHistories();
    }

    public long createOrderHistory(final Member member, final int totalPrice, final int usedPoint, final int orderPrice) {
        final OrderHistoryEntity entity = new OrderHistoryEntity(
                member.getId(),
                totalPrice,
                usedPoint,
                orderPrice
        );
        final OrderHistoryEntity savedEntity = orderHistoryDao.insert(entity);
        return savedEntity.getId();
    }

    public long addOrderProductTo(final long orderHistoryId, final Product product, final int quantity) {
        final OrderProductEntity entity = new OrderProductEntity(
                orderHistoryId,
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                quantity
        );
        final OrderProductEntity savedEntity = orderProductDao.insert(entity);
        return savedEntity.getId();
    }

    public List<Long> findOrderIdsOfMember(final Member member) {
        return orderHistoryDao.findOrderHistoriesByMemberId(member.getId())
                .stream()
                .map(OrderHistoryEntity::getId)
                .collect(Collectors.toList());
    }

    public List<Order> findOrdersOfMember(final Member member) {
        final List<OrderHistoryEntity> orderHistoryEntities = orderHistoryDao.findOrderHistoriesByMemberId(member.getId());

        final List<Order> orders = new ArrayList<>();

        for (final OrderHistoryEntity orderHistory : orderHistoryEntities) {
            final List<OrderProductEntity> products = orderProductDao.findByOrderId(orderHistory.getId());
            final Map<Product, Integer> orderedProducts = products.stream()
                    .collect(Collectors.toMap(
                            orderProduct -> new Product(
                                    orderProduct.getProductId(),
                                    orderProduct.getName(),
                                    orderProduct.getPrice(),
                                    orderProduct.getImageUrl()
                            ),
                            OrderProductEntity::getQuantity));

            final Order order = new Order(
                    orderHistory.getId(),
                    orderHistory.getUsedPoint(),
                    orderedProducts,
                    member
            );
            orders.add(order);
        }

        return orders;
    }
}
