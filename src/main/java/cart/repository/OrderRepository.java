package cart.repository;

import cart.dao.OrderHistoryDao;
import cart.dao.OrderProductDao;
import cart.domain.Member;
import cart.domain.Product;
import cart.entity.OrderHistoryEntity;
import cart.entity.OrderProductEntity;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderHistoryDao orderHistoryDao;
    private final OrderProductDao orderProductDao;

    public OrderRepository(final OrderHistoryDao orderHistoryDao, final OrderProductDao orderProductDao) {
        this.orderHistoryDao = orderHistoryDao;
        this.orderProductDao = orderProductDao;
    }

    public long createOrderHistory(final Member member, final int usedPoint, final int orderPrice) {
        final OrderHistoryEntity entity = new OrderHistoryEntity(member.getId(), usedPoint, orderPrice);
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
                quantity);
        final OrderProductEntity savedEntity = orderProductDao.insert(entity);
        return savedEntity.getId();
    }
}
