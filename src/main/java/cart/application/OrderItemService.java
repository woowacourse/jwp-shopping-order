package cart.application;

import cart.domain.Member;
import cart.domain.order.Order;
import cart.dto.order.OrdersResponse;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class OrderItemService {

    private final OrderRepository orderRepository;

    public OrderItemService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    //    public Long createOrder(Long memberId, OrderItemRequest request) {
//        List<OrderItem> orderItems = request.getOrderItemDtoList().stream()
//                .map(orderItemDto -> new OrderItem(cartItemDao.findById(orderItemDto.getCartItemId()).getProduct(), orderItemDto.getQuantity()))
//                .collect(Collectors.toList());
//
//        int totalPrice = orderItems.stream()
//                .mapToInt(orderItem -> orderItem.getProductPrice() * orderItem.getQuantity())
//                .sum();
//        Order order = new Order(memberId, totalPrice);
//
//        Long orderId = orderDao.createOrder(order);
//        orderItemDao.saveAll(orderId, orderItems);
//        return orderId;
//    }

    public List<OrdersResponse> findAllOrdersByMember(Member member) {
        List<Order> orders = orderRepository.findAllOrdersByMember(member);
        return orders.stream()
                .map(OrdersResponse::from)
                .collect(toList());
    }

}
