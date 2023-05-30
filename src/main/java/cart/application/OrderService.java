package cart.application;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.domain.Order;
import cart.dto.OrderCreateRequest;
import cart.dto.OrderItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private final OrderDao orderDao;
    @Autowired
    private final CartItemDao cartItemDao;

    public OrderService(OrderDao orderDao, CartItemDao cartItemDao) {
        this.orderDao = orderDao;
        this.cartItemDao = cartItemDao;
    }

    public Long createOrder(OrderCreateRequest orderCreateRequest) {
        List<OrderItemRequest> orderItemRequests = orderCreateRequest.getOrderItemRequests();
        List<CartItem> cartItems = orderItemRequests.stream()
                .map(orderItemRequest -> cartItemDao.findById(orderItemRequest.getCartItemId()))
                .collect(Collectors.toList());

        Order order = new Order(cartItems);
        Long id = orderDao.createOrder(order);

        return id;
    }
}
