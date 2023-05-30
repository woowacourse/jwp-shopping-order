package cart.application;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.dto.order.OrderItemRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemService {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final CartItemDao cartItemDao;

    public OrderItemService(OrderDao orderDao, OrderItemDao orderItemDao, CartItemDao cartItemDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.cartItemDao = cartItemDao;
    }

    public Long createOrder(Long memberId, OrderItemRequest request) {
        List<OrderItem> orderItems = request.getOrderItemDtoList().stream()
                .map(orderItemDto -> new OrderItem(cartItemDao.findById(orderItemDto.getCartItemId()).getProduct(), orderItemDto.getQuantity()))
                .collect(Collectors.toList());

        int totalPrice = orderItems.stream()
                .mapToInt(orderItem -> orderItem.getProductPrice() * orderItem.getQuantity())
                .sum();
        Order order = new Order(memberId, totalPrice);

        Long orderId = orderDao.createOrder(order);
        orderItemDao.saveAll(orderId, orderItems);
        return orderId;
    }
}
