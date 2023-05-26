package cart.repository;

import cart.dao.OrderDao;
import cart.dao.OrderProductDao;
import cart.dao.dto.OrderDto;
import cart.dao.dto.OrderProductDto;
import cart.domain.Order;
import cart.domain.OrderProduct;
import java.util.LinkedList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderProductDao orderProductDao;

    public OrderRepository(final OrderDao orderDao, final OrderProductDao orderProductDao) {
        this.orderDao = orderDao;
        this.orderProductDao = orderProductDao;
    }


    public Order save(final Order order) {
        OrderDto orderDto = OrderDto.from(order);
        Long orderId = orderDao.insert(orderDto);

        List<OrderProduct> orderProductsAfterSave = saveOrderProducts(orderId, order.getOrderProducts());

        return new Order(orderId, order.getTimeStamp(), order.getMember(), orderProductsAfterSave);
    }

    private List<OrderProduct> saveOrderProducts(final Long orderId, final List<OrderProduct> orderProducts) {
        List<OrderProduct> orderProductsAfterSave = new LinkedList<>();
        for (OrderProduct orderProduct : orderProducts) {
            OrderProductDto orderProductDto = OrderProductDto.of(orderId, orderProduct);
            Long insert = orderProductDao.insert(orderProductDto);
            orderProductsAfterSave.add(new OrderProduct(insert, orderProduct.getProduct(),
                    orderProduct.getQuantity()));
        }
        return orderProductsAfterSave;
    }
}
