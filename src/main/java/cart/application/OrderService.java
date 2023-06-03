package cart.application;

import cart.dao.OrderDao;
import cart.dao.ProductDao;
import cart.domain.*;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.dto.OrdersResponse;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderDao orderDao;

    private final ProductDao productDao;

    public OrderService(OrderDao orderDao, ProductDao productDao) {
        this.orderDao = orderDao;
        this.productDao = productDao;
    }

    public OrderResponse add(Member member, OrderRequest orderRequest) {
        List<OrderItem> orderItems = orderRequest.getOrderItems().stream()
                .map(orderItemRequest -> {
                    Product product = productDao.getProductById(orderItemRequest.getId());
                    return new OrderItem(product, orderItemRequest.getQuantity());
                })
                .collect(Collectors.toList());

        Long orderId = orderDao.save(new Order(member, new OrderItems(orderItems), 3000L, new Date(2022-5-5)));

        Order order = orderDao.findById(orderId);

        return OrderResponse.of(order);
    }

    public OrderResponse findById(Member member, Long orderId) {
        Order order = orderDao.findById(orderId);
        order.checkOwner(member);
        return OrderResponse.of(order);
    }

    public OrdersResponse findAll(Member member) {
        List<Order> orders = orderDao.findAllByMember(member.getId());
        for (Order order : orders) {
            order.checkOwner(member);
        }
        return OrdersResponse.of(orders);
    }

    public void remove(Member member, Long id) {
        Order order = orderDao.findById(id);
        order.checkOwner(member);

        orderDao.deleteById(id);
    }
}
