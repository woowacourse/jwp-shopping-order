package cart.domain.repository;

import cart.domain.OrderDetail;

import java.util.List;

public interface OrderDetailRepository {
    List<OrderDetail> findAllByOrderId(long orderId);
}
