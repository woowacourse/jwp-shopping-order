package cart.infrastructure;

import cart.dao.OrderDetailDao;
import cart.domain.OrderDetail;
import cart.domain.repository.OrderDetailRepository;
import cart.entity.OrderDetailEntity;
import cart.mapper.OrderDetailMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JdbcOrderDetailRepository implements OrderDetailRepository {

    private final OrderDetailDao orderDetailDao;

    public JdbcOrderDetailRepository(OrderDetailDao orderDetailDao) {
        this.orderDetailDao = orderDetailDao;
    }

    @Override
    public List<OrderDetail> findAllByOrderId(long orderId) {
        List<OrderDetailEntity> orderDetails = orderDetailDao.getAllByOrderId(orderId);
        return orderDetails.stream()
                .map(OrderDetailMapper::toOrderDetail)
                .collect(Collectors.toList());
    }

}
