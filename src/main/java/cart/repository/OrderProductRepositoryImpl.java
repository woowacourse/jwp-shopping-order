package cart.repository;

import cart.dao.OrderProductDao;
import cart.domain.Order;
import cart.domain.repository.OrderProductRepository;
import cart.entity.OrderProductEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderProductRepositoryImpl implements OrderProductRepository {

    private final OrderProductDao orderProductDao;

    public OrderProductRepositoryImpl(OrderProductDao orderProductDao) {
        this.orderProductDao = orderProductDao;
    }

    @Override
    public void save(Long orderSavedId, Order order) {
        List<OrderProductEntity> orderProducts = order.getCartProducts().stream()
                .map(it -> new OrderProductEntity(it.getProduct().getName(),
                        it.getProduct().getImageUrl(), it.getProduct().getPrice(),
                        it.getQuantity(), orderSavedId))
                .collect(Collectors.toList());

        orderProductDao.save(orderSavedId, orderProducts);
    }
}
